package in.solomk.dictionary.ft.client;

import in.solomk.dictionary.api.dto.language.CreateLanguageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;


@Component
@AllArgsConstructor
public class UserLanguagesTestClient {

    private final WebTestClient webTestClient;

    public WebTestClient.ResponseSpec addLanguage(String token, CreateLanguageRequest request) {
        return webTestClient.post()
                            .uri("/api/languages")
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .bodyValue(request)
                            .exchange();
    }

    public WebTestClient.ResponseSpec getLanguages(String token) {
        return webTestClient.get()
                            .uri("/api/languages")
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .exchange();
    }
}

