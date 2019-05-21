package ch.frostnova.spring.boot.mutual.tls;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * X.509 authentication settings
 */
@EnableWebSecurity
public class X509AuthenticationServer extends WebSecurityConfigurerAdapter {

    private final static String ROLE_PREFIX = "ROLE_";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Mutual TLS, extract user name from client certificate (CN field)
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .x509()
                .subjectPrincipalRegex("CN=([^,]*)")
                .userDetailsService(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Minimal implementation of a user detail service: user with name and roles.
        return username -> new User(username, "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        getRolesForUser(username).stream()
                                .map(r -> ROLE_PREFIX + r)
                                .collect(Collectors.joining(","))));
    }

    private Set<String> getRolesForUser(String username) {

        // Get roles for user from configuration or a directory (LDAP),
        // or extract roles propagated by JWT or SAML or header (WAF).
        // Here we just return a fixed set of roles for any authenticated user.
        return Stream.of("Test-User", "Admin").collect(Collectors.toSet());
    }
}