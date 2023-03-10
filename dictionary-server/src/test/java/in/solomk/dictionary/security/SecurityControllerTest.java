package in.solomk.dictionary.security;

import in.solomk.dictionary.api.RouteConfiguration;
import in.solomk.dictionary.api.handler.AuthHandler;
import in.solomk.dictionary.api.handler.language.AddLanguageHandler;
import in.solomk.dictionary.api.handler.language.DeleteLanguageHandler;
import in.solomk.dictionary.api.handler.language.GetLanguagesHandler;
import in.solomk.dictionary.api.handler.profile.ProfileHandler;
import in.solomk.dictionary.api.handler.settings.GetLanguageSettingsHandler;
import in.solomk.dictionary.api.handler.words.AddWordHandler;
import in.solomk.dictionary.api.handler.words.DeleteWordHandler;
import in.solomk.dictionary.api.handler.words.GetWordsHandler;
import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.config.SecurityConfiguration;
import in.solomk.dictionary.service.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({RouteConfiguration.class,
        SecurityConfiguration.class,
        TokenService.class,
        ProfileHandler.class,
        AuthHandler.class})
@MockBeans({
        @MockBean(UserProfileService.class),
        @MockBean(GetWordsHandler.class),
        @MockBean(AddWordHandler.class),
        @MockBean(GetLanguagesHandler.class),
        @MockBean(AddLanguageHandler.class),
        @MockBean(DeleteLanguageHandler.class),
        @MockBean(GetLanguageSettingsHandler.class),
        @MockBean(DeleteWordHandler.class)
})
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

}
