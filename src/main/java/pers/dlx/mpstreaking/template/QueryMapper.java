package pers.dlx.mpstreaking.template;

import java.util.List;
import java.util.Map;

/**
 * 通用自定义sql查询接口
 *
 * @author dinglingxiang  2020/3/19 15:12
 */
public interface QueryMapper {
    List<Map<String, Object>> query(String sql);
}
