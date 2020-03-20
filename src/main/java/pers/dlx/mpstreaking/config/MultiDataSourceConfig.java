package pers.dlx.mpstreaking.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import pers.dlx.mpstreaking.ds.DBTypeEnum;
import pers.dlx.mpstreaking.ds.DynamicDataSource;
import pers.dlx.mpstreaking.streaking.StreakingMybatisSqlSessionFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 *
 * @author dinglingxiang
 */
@Configuration
public class MultiDataSourceConfig {

    @Value("${stat.h2.initFlag}")
    private boolean h2InitFlag;

    @Bean(name = "stat1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.stat1")
    public DataSource stat1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "stat2")
    @ConfigurationProperties(prefix = "spring.datasource.druid.stat2")
    public DataSource stat2() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 数据源初始化时 执行脚本构造数据  （开发环境执行 正式环境不执行）
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer1(@Qualifier("stat1") DataSource datasource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(datasource);

        if (h2InitFlag) {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
            resourceDatabasePopulator.addScript(new ClassPathResource("db1/schema-h2.sql"));
            resourceDatabasePopulator.addScript(new ClassPathResource("db1/data-h2.sql"));
            dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        }

        return dataSourceInitializer;
    }

    /**
     * 数据源初始化时 执行脚本构造数据  （开发环境执行 正式环境不执行）
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer2(@Qualifier("stat2") DataSource datasource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(datasource);
        if (h2InitFlag) {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
            resourceDatabasePopulator.addScript(new ClassPathResource("db2/schema-h2.sql"));
            resourceDatabasePopulator.addScript(new ClassPathResource("db2/data-h2.sql"));
            dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        }

        return dataSourceInitializer;
    }

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("stat1") DataSource stat1,
                                         @Qualifier("stat2") DataSource stat2) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.stat1.getValue(), stat1);
        targetDataSources.put(DBTypeEnum.stat2.getValue(), stat2);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(stat1);
        return dynamicDataSource;
    }

    /**
     * 分页插件
     *
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        // 自定义的支持裸奔的StreakingMybatisSqlSessionFactoryBean
        StreakingMybatisSqlSessionFactoryBean sqlSessionFactory = new StreakingMybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(stat1(), stat2()));
        Resource[] resources = ArrayUtils.addAll(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"),
                new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*/*.xml"));
        sqlSessionFactory.setMapperLocations(resources);


        //添加分页功能
        sqlSessionFactory.setPlugins(
                // PerformanceInterceptor(),OptimisticLockerInterceptor()
                paginationInterceptor());
        return sqlSessionFactory.getObject();
    }
}