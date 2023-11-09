package may.yuntian.common.config;


import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * spring boot同时支持http和https访问
 * Springboot中启用多个监听端口
 *  同时启用两个端口port(s): 8443(https) 与 8080(http)
 *  需要在application.yml中添加http.port配置
 *  在启动函数中引用此配置文件即可@Import({EmbeddedTomcatConfiguration.class})
 * @author MaYong
 * @date 2018-11-13
 */
//@Configuration
public class EmbeddedTomcatConfiguration {
	@Value("${http.port}")
    private Integer port;
 
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
		return tomcat;
	}
	
	// 配置http
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(port);
        return connector;
    }
 
    

}
