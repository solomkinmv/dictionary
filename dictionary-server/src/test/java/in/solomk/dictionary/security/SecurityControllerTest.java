package in.solomk.dictionary.security;

import in.solomk.dictionary.api.RouteConfiguration;
import in.solomk.dictionary.api.handler.AddWordHandler;
import in.solomk.dictionary.api.handler.AuthHandler;
import in.solomk.dictionary.api.handler.GetWordsHandler;
import in.solomk.dictionary.api.handler.ProfileHandler;
import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.config.SecurityConfiguration;
import in.solomk.dictionary.service.user.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({RouteConfiguration.class,
        SecurityConfiguration.class,
        TokenService.class,
        ProfileHandler.class,
        AuthHandler.class,
        SecurityControllerTest.TestConfig.class})
@ActiveProfiles("test")
@RegisterReflectionForBinding(value = GetWordsHandler.class)
public class SecurityControllerTest {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenUnauthenticatedReturns401() {
        webTestClient.get()
                     .uri("/api/me")
                     .exchange()
                     .expectStatus()
                     .isUnauthorized();
    }

    @Test
    void whenAuthenticatedPrintsHello() {
        String token = tokenService.generateToken("someUserId");

        webTestClient.get()
                     .uri("/api/me")
                     .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.message", "Hello, max");
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserProfileService userProfileService() {
            return new UserProfileService(null);
        }

        @Bean
        public GetWordsHandler getWordsHandler() {
            return new GetWordsHandler(null, null);
        }

        @Bean
        public AddWordHandler addWordHandler() {
            return new AddWordHandler(null, null);
        }
    }
}
