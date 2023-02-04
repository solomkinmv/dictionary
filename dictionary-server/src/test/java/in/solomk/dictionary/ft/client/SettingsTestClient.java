package in.solomk.dictionary.ft.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;


@Component
@AllArgsConstructor
public class SettingsTestClient {

    private final WebTestClient webTestClient;

    public WebTestClient.ResponseSpec getSupportedLanguages(String token) {
        return webTestClient.get()
                            .uri("/api/settings/languages")
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                            .exchange();
    }

}

