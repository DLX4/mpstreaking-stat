package pers.dlx.mpstreaking.streaking;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dinglingxiang  2020/3/16 14:22
 */
@Configuration
public class StreakingConfig {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean(name = "streakingMapperFactoryBean")
    public StreakingMapperFactoryBean streakingMapperFactoryBeanFactory() {
        return new StreakingMapperFactoryBean(sqlSessionFactory);
    }
}
