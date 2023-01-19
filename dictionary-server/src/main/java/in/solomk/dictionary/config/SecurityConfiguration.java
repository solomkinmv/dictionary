package in.solomk.dictionary.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.service.user.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfiguration {

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
    public ReactiveJwtDecoder reactiveJwtDecoder(RsaKeyProperties rsaKeyProperties) {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKeyProperties.publicKey())
                                       .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RsaKeyProperties rsaKeyProperties) {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.publicKey())
                .privateKey(rsaKeyProperties.privateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public RsaKeyProperties rsaKeyProperties(@Value("${rsa.public-key-content:}") String publicKeyContent,
                                             @Value("${rsa.private-key-content:}") String privateKeyContent,
                                             @Value("${rsa.public-key:#{null}}") RSAPublicKey publicKey,
                                             @Value("${rsa.private-key:#{null}}") RSAPrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Using RSA keys from application properties [publicKeyPresent={}, privateKeyPresent={}, publicKeyContent length = {}, privateKeyContent length = {}]",
                 publicKey != null, privateKey != null, publicKeyContent.length(), privateKeyContent.length());

        KeyFactory kf = KeyFactory.getInstance("RSA");

        if (publicKey == null) {
            if (publicKeyContent.isEmpty()) {
                throw new IllegalStateException("RSA public key is not configured");
            }
            publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent)));
        }
        if (privateKey == null) {
            if (privateKeyContent.isEmpty()) {
                throw new IllegalStateException("RSA private key is not configured");
            }
            privateKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent)));
        }
        return new RsaKeyProperties(publicKey, privateKey);
    }
}
