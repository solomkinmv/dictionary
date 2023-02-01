package in.solomk.dictionary.ft.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;


@Component
@AllArgsConstructor
public class UserLanguagesTestClient {

    private final WebTestClient webTestClient;

    public WebTestClient.ResponseSpec addLanguage(String token, String languageCode) {
        return webTestClient.post()
                            .uri("/api/languages/{languageCode}", languageCode)
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .exchange();
    }

    public WebTestClient.ResponseSpec deleteLanguage(String token, String languageCode) {
        return webTestClient.delete()
                            .uri("/api/languages/{languageCode}", languageCode)
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .exchange();
    }

    public WebTestClient.ResponseSpec getLanguages(String token) {
        return webTestClient.get()
                            .uri("/api/languages")
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .exchange();
    }
}

