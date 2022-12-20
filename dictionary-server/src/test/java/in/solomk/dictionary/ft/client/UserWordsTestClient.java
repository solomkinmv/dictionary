package in.solomk.dictionary.ft.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.solomk.dictionary.api.dto.CreateWordRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Component
public class UserWordsTestClient {

    private final WebTestClient webTestClient;

    public UserWordsTestClient(ApplicationContext context, ObjectMapper mapper) {
        var exchangeStrategiesWithCustomObjectMapper =
                ExchangeStrategies.builder()
                                  .codecs(configurer -> configurer.defaultCodecs()
                                                                  .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
                                  .build();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                                     .configureClient()
                                     .exchangeStrategies(exchangeStrategiesWithCustomObjectMapper)
                                     .build();
    }

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

    public WebTestClient.ResponseSpec addWord(String userId, CreateWordRequest request) {
        return webTestClient.post()
                            .uri("/api/users/{id}/words", userId)
                            .bodyValue(request)
                            .exchange();
    }

    public WebTestClient.ResponseSpec getUserWords(String userId) {
        return webTestClient.get()
                            .uri("/api/users/{id}/words", userId)
                            .exchange();
    }
}
