package pers.dlx.mpstreaking.streaking;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.dlx.mpstreaking.template.QueryMapper;
import pers.dlx.mpstreaking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.apache.ibatis.mapping.StatementType.STATEMENT;

/**
 * @author dinglingxiang  2020/3/18 10:36
 */
@Component
@Slf4j
public class StreakingMybatisUtilMapper implements QueryMapper {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Resource(name = "&streakingMapperFactoryBean")
    private StreakingMapperFactoryBean streakingMapperFactoryBean;

    @Override
    public List<Map<String, Object>> query(String sql) {
        StreakingMybatisConfiguration configuration = (StreakingMybatisConfiguration) sqlSessionFactory.getConfiguration();

        String uuid = SnowflakeIdWorker.getNextId() + "";
        StatementUuid.set(uuid);
        try {
            MapperBuilderAssistant mapperBuilderAssistant = new MapperBuilderAssistant(configuration, "");
            mapperBuilderAssistant.setCurrentNamespace("pers.dlx.mpstreaking.streaking.StreakingMapper");
            mapperBuilderAssistant.addMappedStatement(
                    "select" + uuid, new StaticSqlSource(configuration, sql),
                    STATEMENT, SqlCommandType.SELECT,
                    null,
                    null,
                    null, null, null, Map.class, null,
                    false, false, false, NoKeyGenerator.INSTANCE,
                    null, null, null,
                    configuration.getLanguageDriver(null), null
            );

            StreakingMapper streakingMapper = (StreakingMapper) streakingMapperFactoryBean.getObject();

            List<Map<String, Object>> list = streakingMapper.select();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error: {}", e.getMessage(), e);
            return Lists.newArrayList();
        } finally {
            configuration.removeMappedStatement("com.zjipst.yun110.stat.streaking.StreakingMapper.select" + uuid);
        }
    }
}
