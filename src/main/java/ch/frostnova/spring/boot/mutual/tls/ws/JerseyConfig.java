package ch.frostnova.spring.boot.mutual.tls.ws;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import ch.frostnova.spring.boot.mutual.tls.ws.provider.CORSFilter;
import ch.frostnova.spring.boot.mutual.tls.ws.provider.NoSuchElementExceptionMapper;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RS configuration
 */
@Configuration
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        registerEndpoints();
        configureSwagger();
    }

    private void registerEndpoints() {
        register(NotesEndpoint.class);
        register(CORSFilter.class);
        register(NoSuchElementExceptionMapper.class);
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("ch.frostnova.spring.boot.mutual.tls.ws");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }
}
