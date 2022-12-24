package in.solomk.dictionary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler());

        return http
                .csrf()
                    .disable()
                .authorizeExchange()
                    .anyExchange()
                    .authenticated()
                    .and()
                .oauth2Login()
                    .and()
                .logout()
                    .logoutHandler(logoutHandler)
                    .and()
                .build();
    }

}
