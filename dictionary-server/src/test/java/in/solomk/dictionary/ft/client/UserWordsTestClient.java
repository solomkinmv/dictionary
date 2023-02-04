package in.solomk.dictionary.ft.client;

import in.solomk.dictionary.api.dto.CreateWordRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;


@Component
@AllArgsConstructor
public class UserWordsTestClient {

    private final WebTestClient webTestClient;

    public WebTestClient.ResponseSpec getIndexPage() {
        return webTestClient.get()
                            .uri("/")
                            .exchange();
    }

    public WebTestClient.ResponseSpec get404Page() {
        return webTestClient.get()
                            .uri("/404")
                            .exchange();
    }

    public WebTestClient.ResponseSpec getCustomPage() {
        return webTestClient.get()
                            .uri("/custom-page")
                            .exchange();
    }

    public WebTestClient.ResponseSpec addWord(String token, String languageCode, CreateWordRequest request) {
        return webTestClient.post()
                            .uri("/api/languages/{languageCode}/words", languageCode)
                            .header("Authorization", "Bearer " + token)
                            .bodyValue(request)
                            .exchange();
    }

    public WebTestClient.ResponseSpec getUserWords(String token, String languageCode) {
        return webTestClient.get()
                            .uri("/api/languages/{languageCode}/words", languageCode)
                            .header("Authorization", "Bearer " + token)
                            .exchange();
    }
}

