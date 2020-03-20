package pers.dlx.mpstreaking.streaking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author dinglingxiang  2020/3/16 10:58
 */
public interface StreakingMapper extends BaseMapper {

    List<Map<String, Object>> select();

}
