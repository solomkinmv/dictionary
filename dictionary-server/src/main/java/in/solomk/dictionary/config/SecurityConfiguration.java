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
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
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
    SecurityWebFilterChain filterChain(ServerHttpSecurity http,
                                       ServerAuthenticationSuccessHandler jwtServerAuthenticationSuccessHandler,
                                       ReactiveAuthenticationManager httpBasicAuthenticationManager) {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        // @formatter:off
        return http
                .csrf().disable()
                       .cors(corsSpec -> corsSpec.configurationSource(request -> config))
                       .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange()
                    .pathMatchers("/api/**").authenticated()
                    .pathMatchers("/actuator/health").permitAll()
                    .pathMatchers("/actuator/**").hasRole("ACTUATOR")
                    .pathMatchers("/**").permitAll()
                    .and()
                .httpBasic()
                    .authenticationManager(httpBasicAuthenticationManager)
                    .and()
                .oauth2ResourceServer()
                    .jwt()
                        .and()
                        .and()
                .oauth2Login()
                    .authenticationSuccessHandler(jwtServerAuthenticationSuccessHandler)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
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
    UserDetailsRepositoryReactiveAuthenticationManager httpBasicAuthenticationManager(
            ReactiveUserDetailsService httpBasicUserDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(httpBasicUserDetailsService);
    }

    @Bean
    MapReactiveUserDetailsService httpBasicUserDetailsService(@Value("${security.user.name}") String username,
                                                              @Value("${security.user.password}") String password,
                                                              PasswordEncoder passwordEncoder) {
        return new MapReactiveUserDetailsService(User.builder()
                                                         .username(username)
                                                         .password(passwordEncoder.encode(password))
                                                         .roles("ACTUATOR")
                                                         .build());
    }

    @Bean
    public ServerAuthenticationSuccessHandler jwtServerAuthenticationSuccessHandler(
            TokenService tokenService,
            UserProfileService userProfileService,
            @Value("${security.redirect.web:}") String redirectWeb) {

        return (webFilterExchange, authentication) ->
                userProfileService.getUserProfileBySocialProviderId("google", authentication.getName()) // currently support only google
                        .map(userProfile -> tokenService.generateToken(userProfile.id()))
                        .doOnNext(token -> {
                            log.debug("Generated JWT: {}", token);
                            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);
                            response.getHeaders().setLocation(URI.create(redirectWeb + "/authorized?jwt=" + token));
                        })
                        .then();
    }

    @Bean
    @Primary
    public DelegatingReactiveAuthenticationManager delegatingReactiveAuthenticationManager(ReactiveAuthenticationManager jwtAuthenticationManager,
                                                                                           ReactiveAuthenticationManager httpBasicAuthenticationManager) {
        return new DelegatingReactiveAuthenticationManager(jwtAuthenticationManager, httpBasicAuthenticationManager);
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
                                             @Value("${rsa.public-key:#{null}}") String publicKeyPath,
                                             @Value("${rsa.private-key:#{null}}") String privateKeyPath) throws GeneralSecurityException, IOException {
        log.info("Using RSA keys from application properties [publicKeyPresent={}, privateKeyPresent={}, publicKeyContent length = {}, privateKeyContent length = {}]",
                 publicKeyPath != null, privateKeyPath != null, publicKeyContent.length(), privateKeyContent.length());

        RSAPublicKey publicKey;
        if (publicKeyPath == null) {
            if (publicKeyContent.isEmpty()) {
                throw new IllegalStateException("RSA public key is not configured");
            }
            publicKey = JavaSecurityPemUtils.parseX509PublicKey(publicKeyContent);
        } else {
            publicKey = JavaSecurityPemUtils.readX509PublicKey(new File(publicKeyPath));
        }

        RSAPrivateKey privateKey;
        if (privateKeyPath == null) {
            if (privateKeyContent.isEmpty()) {
                throw new IllegalStateException("RSA private key is not configured");
            }
            privateKey = JavaSecurityPemUtils.parsePKSC8PrivateKey(privateKeyContent);
        } else {
            privateKey = JavaSecurityPemUtils.readPKCS8PrivateKey(new File(privateKeyPath));
        }
        return new RsaKeyProperties(publicKey, privateKey);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

class JavaSecurityPemUtils {

    public static RSAPrivateKey readPKCS8PrivateKey(File file) throws GeneralSecurityException, IOException {
        String key = Files.readString(file.toPath(), Charset.defaultCharset());

        return parsePKSC8PrivateKey(key);
    }

    public static RSAPrivateKey parsePKSC8PrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getMimeDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static RSAPublicKey readX509PublicKey(File file) throws GeneralSecurityException, IOException {
        String key = Files.readString(file.toPath(), Charset.defaultCharset());

        return parseX509PublicKey(key);
    }

    public static RSAPublicKey parseX509PublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getMimeDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

}
