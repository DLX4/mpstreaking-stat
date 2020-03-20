package pers.dlx.mpstreaking.streaking;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.executor.MybatisBatchExecutor;
import com.baomidou.mybatisplus.core.executor.MybatisReuseExecutor;
import com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;

/**
 * 完全拷贝自MybatisConfiguration， 新增了removeMappedStatement方法，
 *
 * @author dinglingxiang  2020/3/16 15:19
 */

public class StreakingMybatisConfiguration extends MybatisConfiguration {

    private static final Log logger = LogFactory.getLog(StreakingMybatisConfiguration.class);

    /**
     * Mapper 注册
     */
    protected final StreakingMybatisMapperRegistry mybatisMapperRegistry = new StreakingMybatisMapperRegistry(this);

    //StreakingMybatisMapperRegistry


    // TODO 自己的 GlobalConfig
    @Setter
    @Getter
    private GlobalConfig globalConfig = GlobalConfigUtils.defaults();

    public StreakingMybatisConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    /**
     * 初始化调用
     */
    public StreakingMybatisConfiguration() {
        super();
        this.mapUnderscoreToCamelCase = true;
        languageRegistry.setDefaultDriverClass(MybatisXMLLanguageDriver.class);
    }


    /**
     * MybatisPlus 加载 SQL 顺序：
     * <p>1、加载XML中的SQL</p>
     * <p>2、加载sqlProvider中的SQL</p>
     * <p>3、xmlSql 与 sqlProvider不能包含相同的SQL</p>
     * <p>调整后的SQL优先级：xmlSql > sqlProvider > curdSql</p>
     */
    @Override
    public void addMappedStatement(MappedStatement ms) {
        super.addMappedStatement(ms);
    }

    public void removeMappedStatement(String statementid) {
        if (mappedStatements.containsKey(statementid)) {
            mappedStatements.remove(statementid);
        }
    }


    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public MapperRegistry getMapperRegistry() {
        return mybatisMapperRegistry;
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mybatisMapperRegistry.addMappers(packageName, superType);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public void addMappers(String packageName) {
        mybatisMapperRegistry.addMappers(packageName);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type);
    }

    /**
     * 指定动态SQL生成的默认语言
     *
     * @param driver LanguageDriver
     */
    @Override
    public void setDefaultScriptingLanguage(Class<? extends LanguageDriver> driver) {
        if (driver == null) {
            //todo 替换动态SQL生成的默认语言为自己的。
            driver = MybatisXMLLanguageDriver.class;
        }
        getLanguageRegistry().setDefaultDriverClass(driver);
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new MybatisBatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new MybatisReuseExecutor(this, transaction);
        } else {
            executor = new MybatisSimpleExecutor(this, transaction);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }
}
