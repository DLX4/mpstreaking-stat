package pers.dlx.mpstreaking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan({"pers.dlx.mpstreaking"})
@SpringBootApplication(scanBasePackages = "pers.dlx", exclude = DataSourceAutoConfiguration.class)
// @Mapper 注解。
public class MpstreakingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpstreakingApplication.class, args);
    }

}