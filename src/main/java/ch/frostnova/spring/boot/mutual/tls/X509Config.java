package ch.frostnova.spring.boot.mutual.tls;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * X509 authentication configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class X509Config {

    private final static String ROLE_PREFIX = "ROLE_";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .anyRequest()
                        .authenticated())
                .x509(x509 -> x509
                        .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                        .userDetailsService(userDetailsService()));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Minimal implementation of a user detail service: user with name and roles.
        return username -> new User(username, "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        getRolesForUser(username).stream()
                                .map(r -> ROLE_PREFIX + r)
                                .collect(joining(","))));
    }

    private Set<String> getRolesForUser(String username) {

        // Get roles for user from configuration or a directory (LDAP),
        // or extract roles propagated by JWT or SAML or header (WAF).
        // Here we just return a fixed set of roles for any authenticated user.
        return Stream.of("Test-User", "Admin").collect(Collectors.toSet());
    }
}
