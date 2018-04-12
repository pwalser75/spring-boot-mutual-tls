package ch.frostnova.spring.boot.mutual.tls;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot application main class
 */
@SpringBootApplication
public class SpringBootProject {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

        return args -> {
            // add any initialization code here
        };
    }
}
