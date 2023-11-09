package may.yuntian;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author zx
 * @date 2022年2月22日11:02:45
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//@EnableDiscoveryClient
//@EnableFeignClients
public class AnlianZjApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AnlianZjApplication.class, args);
        System.out.println("项目启动成功......");

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AnlianZjApplication.class);
    }

}
