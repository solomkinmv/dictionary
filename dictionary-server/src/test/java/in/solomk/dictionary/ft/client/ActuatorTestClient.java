package in.solomk.dictionary.ft.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class ActuatorTestClient {

    private final WebTestClient webTestClient;

    public ResponseSpec getHealth(AuthenticationMode authenticationMode) {
        return webTestClient.get()
                            .uri("/actuator/health")
                            .headers(addAuthHeader(authenticationMode))
                            .exchange();
    }

    public ResponseSpec getInfo(AuthenticationMode authenticationMode) {
        return webTestClient.get()
                            .uri("/actuator/info")
                            .headers(addAuthHeader(authenticationMode))
                            .exchange();
    }

    public ResponseSpec getAllActuatorEndpoints(AuthenticationMode authenticationMode) {
        return webTestClient.get()
                            .uri("/actuator")
                            .headers(addAuthHeader(authenticationMode))
                            .exchange();
    }

    public ResponseSpec getEnv(AuthenticationMode authenticationMode) {
        return webTestClient.get()
                            .uri("/actuator/env")
                            .headers(addAuthHeader(authenticationMode))
                            .exchange();
    }

    public ResponseSpec getMetrics(AuthenticationMode authenticationMode) {
        return webTestClient.get()
                            .uri("/actuator/metrics")
                            .headers(addAuthHeader(authenticationMode))
                            .exchange();
    }


    private Consumer<HttpHeaders> addAuthHeader(AuthenticationMode authenticationMode) {
        return headers -> {
            if (authenticationMode == AuthenticationMode.AUTHENTICATED) {
                headers.setBasicAuth("admin", "secret");
            }
            if (authenticationMode == AuthenticationMode.INVALID_CREDENTIALS) {
                headers.setBasicAuth("admin", "invalid");
            }
        };
    }

    public enum AuthenticationMode {
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_CREDENTIALS
    }
}
