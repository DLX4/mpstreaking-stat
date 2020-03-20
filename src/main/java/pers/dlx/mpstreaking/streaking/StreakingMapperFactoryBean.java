package pers.dlx.mpstreaking.streaking;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;

/**
 * @author dinglingxiang  2020/3/16 13:58
 */
public class StreakingMapperFactoryBean extends MapperFactoryBean<StreakingMapper> {

    public StreakingMapperFactoryBean(SqlSessionFactory sqlSessionFactory) {
        super(StreakingMapper.class);
        this.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreakingMapper getObject() throws Exception {
        return getSqlSession().getMapper(StreakingMapper.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<StreakingMapper> getObjectType() {
        return StreakingMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
