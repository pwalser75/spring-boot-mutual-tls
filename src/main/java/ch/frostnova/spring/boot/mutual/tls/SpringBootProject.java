package ch.frostnova.spring.boot.mutual.tls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Spring boot application main class
 */
@SpringBootApplication
@ServletComponentScan
public class SpringBootProject {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject.class, args);
    }

}
