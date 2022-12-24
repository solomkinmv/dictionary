package in.solomk.dictionary.security;

import in.solomk.dictionary.api.RouteConfiguration;
import in.solomk.dictionary.api.handler.AddWordHandler;
import in.solomk.dictionary.api.handler.AuthHandler;
import in.solomk.dictionary.api.handler.GetWordsHandler;
import in.solomk.dictionary.api.handler.ProfileHandler;
import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({RouteConfiguration.class,
        SecurityConfiguration.class,
        TokenService.class,
        ProfileHandler.class,
        AuthHandler.class})
@MockBean(GetWordsHandler.class)
@MockBean(AddWordHandler.class)
@RegisterReflectionForBinding(value = GetWordsHandler.class)
public class SecurityControllerTest {

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
        String token = webTestClient.post()
                                    .uri("/api/token")
                                    .headers(httpHeaders -> httpHeaders.setBasicAuth("max", "password"))
                                    .exchange()
                                    .expectStatus().isOk()
                                    .returnResult(String.class)
                                    .getResponseBody()
                                    .blockFirst();

        webTestClient.get()
                     .uri("/api/me")
                     .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.message", "Hello, max");
    }

}
