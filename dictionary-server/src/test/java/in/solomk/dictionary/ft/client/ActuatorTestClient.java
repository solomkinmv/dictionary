package in.solomk.dictionary.ft.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class ActuatorTestClient {

    private final WebTestClient webTestClient;

    public WebTestClient.ResponseSpec getHealth(boolean authenticated) {
        return webTestClient.get()
                            .uri("/actuator/health")
                            .headers(addAuthHeader(authenticated))
                            .exchange();
    }

    public WebTestClient.ResponseSpec getInfo(boolean authenticated) {
        return webTestClient.get()
                            .uri("/actuator/info")
                            .headers(addAuthHeader(authenticated))
                            .exchange();
    }


    private Consumer<HttpHeaders> addAuthHeader(boolean authenticated) {
        return headers -> {
            if (authenticated) {
                headers.setBasicAuth("admin", "secret");
            }
        };
    }
}
