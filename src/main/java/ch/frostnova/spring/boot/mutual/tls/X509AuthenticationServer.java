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
 *
 * @author wap
 * @since 13.04.2018
 */
@EnableWebSecurity
public class X509AuthenticationServer extends WebSecurityConfigurerAdapter {

    private final static String ROLE_PREFIX = "ROLE_";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .x509()
                .subjectPrincipalRegex("CN=([^,]*)")
                .userDetailsService(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> new User(username, "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        getRolesForUser(username).stream()
                                .map(r -> ROLE_PREFIX + r)
                                .collect(Collectors.joining(","))));
    }

    private Set<String> getRolesForUser(String username) {
        return Stream.of("Test-User", "Admin").collect(Collectors.toSet());
    }
}