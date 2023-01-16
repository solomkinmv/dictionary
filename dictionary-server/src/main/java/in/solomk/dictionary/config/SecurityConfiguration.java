package in.solomk.dictionary.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.service.user.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(RsaKeyProperties.class)
@AllArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final RsaKeyProperties rsaKeyProperties;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http,
                                              ServerAuthenticationSuccessHandler jwtServerAuthenticationSuccessHandler) {
        // @formatter:off
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        return http
                .csrf().disable()
                .cors(corsSpec -> corsSpec.configurationSource(request -> config))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange()
                    .anyExchange()
                    .authenticated()
                    .and()
                .oauth2ResourceServer()
                    .jwt()
                        .and()
                    .and()
                .oauth2Login()
                    .authenticationSuccessHandler(jwtServerAuthenticationSuccessHandler)
                    .and()
                .build();
        // @formatter:on
    }

    @Bean
    CorsWebFilter permissiveCorsFilter() {
        return new CorsWebFilter(exchange -> {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            return config;
        });
    }

    @Bean
    public ServerAuthenticationSuccessHandler jwtServerAuthenticationSuccessHandler(
            TokenService tokenService,
            UserProfileService userProfileService) {

        return (webFilterExchange, authentication) ->
                userProfileService.getUserProfileBySocialProviderId("google", authentication.getName()) // currently support only google
                        .map(userProfile -> tokenService.generateToken(userProfile.id()))
                        .doOnNext(token -> {
                            log.debug("Generated JWT: {}", token);
                            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);
                            response.getHeaders().setLocation(URI.create("http://localhost:3000/authorized?jwt=" + token));
                        })
                        .then();
    }

    @Bean
    public ReactiveUserDetailsService jwtMappingUserDetailsService(UserProfileService userProfileService) {
        return username -> userProfileService.getByUserId(username)
                .map(userProfile -> User.builder()
                        .username(userProfile.id())
                        .build());
    }

    @Bean
    public ReactiveAuthenticationManager jwtAuthenticationManager(ReactiveJwtDecoder jwtDecoder) {
        return new JwtReactiveAuthenticationManager(jwtDecoder);
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKeyProperties.publicKey())
                                       .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.publicKey())
                .privateKey(rsaKeyProperties.privateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
