package ch.frostnova.spring.boot.mutual.tls;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat settings: enforce HTTPS, redirect from HTTP port to HTTPS.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class TomcatSettings {

    @Value("${http.server.port}")
    private int serverPortHttp;

    @Value("${server.port}")
    private int serverPortHttps;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatFactory();
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }


    static class TomcatFactory extends TomcatServletWebServerFactory {
        @Override
        protected void postProcessContext(Context context) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
        }
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(Http11NioProtocol.class.getName());
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(serverPortHttp);
        connector.setRedirectPort(serverPortHttps);
        return connector;
    }
}
