package pers.dlx.mpstreaking.template;

import org.springframework.util.CollectionUtils;
import pers.dlx.mpstreaking.ds.DbContextHolder;
import pers.dlx.mpstreaking.utils.EncryptUtil;
import pers.dlx.mpstreaking.utils.LogUtil;
import pers.dlx.mpstreaking.utils.Timespan;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JtSqlEngine {
    private ScriptEngine jsEngine = null;
    private JTAPI jtapi = null;
    private QueryMapper queryMapper;


    public JtSqlEngine(QueryMapper queryMapper) {
        this.queryMapper = queryMapper;
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        jsEngine = scriptEngineManager.getEngineByName("nashorn");

        jtapi = new JTAPI();

        jsEngine.put("JTAPI", jtapi);

        try {

            jsEngine.eval("function guid(){return JTAPI.guid()};");
            jsEngine.eval("function md5(txt){return JTAPI.md5(txt)};");
            jsEngine.eval("function sha1(txt){return JTAPI.sha1(txt)};");

            jsEngine.eval("function set(key,obj){JTAPI.set(key,JSON.stringify(obj))};");
            jsEngine.eval("function get(key){var txt=JTAPI.get(key);if(txt){return JSON.parse(txt)}else{return null}};");

            jsEngine.eval("function log(txt){JTAPI.log(txt)};");
            jsEngine.eval("function sql(txt){var _d=JTAPI.sql(txt);if(_d&&typeof(_d)=='object'&&_d.getRow){var item=_d.getRow(0);var keys=item.keys();if(_d.getRowCount()==1){var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=item.get(k1)};return obj}else{var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=m1.get(k1)};ary.push(obj)}};return ary}}else{return _d}};");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Object exec(String jtsql, Map<String, String> params) throws ScriptException {
        jtapi.clear();

        if (!CollectionUtils.isEmpty(params)) {
            for (String param : params.keySet()) {
                jtapi.set(param, params.get(param));
            }
        }

        String jscode = "(function(){" + compile(jtsql) + "})();";

        return jsEngine.eval(jscode);
    }

    public String last_sql() {
        return jtapi.last_sql;
    }

    private String compile(String jtsql) {
        String jscode = jtsql.replaceAll("\r\n", " ");

        jscode = jscode.replaceAll("\\$\\<", "sql(\"");
        jscode = jscode.replaceAll("\\>;", "\");");

        jscode = jscode.replaceAll("\\{\\{", "\"+");
        jscode = jscode.replaceAll("\\}\\}", "+\"");

        LogUtil.write("JtSQL.code", jscode);

        return jscode;
    }

    public class JTAPI {
        public String last_sql = null;
        private Map<String, String> setting = new HashMap<>();


        public JTAPI() {
        }

        public Object sql(String sql) throws SQLException {
            this.last_sql = sql;

            boolean isLog = (sql.endsWith("::nolog") == false);

            if (isLog == false) {
                sql = sql.replace("::nolog", "");
            }

            if (sql.indexOf("::") > 0) {
                // 切换数据源
                String[] ss = sql.split("::");

                sql = ss[1];
                DbContextHolder.setDbType(ss[0]);
            }


            Date start_time = new Date();
            Object temp = doSql(sql);

            long exec_time = new Timespan(start_time).milliseconds();

            if (isLog) {
                LogUtil.write("EXEC.TIME", (exec_time / 1000.0) + "s::" + sql);
            }

            return temp;
        }

        private Object doSql(String sql) throws SQLException {
            return queryMapper.query(sql);
        }

        public void log(Object txt) {
            LogUtil.write("JtSQL.log", txt.toString());
        }

        public String guid() {
            return UUID.randomUUID().toString();
        }

        public String md5(String str) {
            return EncryptUtil.md5(str);
        }

        public String sha1(String str) {
            return EncryptUtil.sha1(str);
        }

        public void set(String key, String val) {
            setting.put(key, val);
        }

        public String get(String key) {
            if (setting.containsKey(key)) {
                return setting.get(key);
            } else {
                return null;
            }
        }

        public void clear() {
            setting.clear();
        }

    }
}