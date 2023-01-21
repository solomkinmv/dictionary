package in.solomk.dictionary.ft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
public class WebTestClientConfiguration {

    @Bean
    WebTestClient webTestClient(ApplicationContext context, ObjectMapper mapper) {
        var exchangeStrategiesWithCustomObjectMapper =
                ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                                        .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
                        .build();
        return WebTestClient.bindToApplicationContext(context)
                            .configureClient()
                            .exchangeStrategies(exchangeStrategiesWithCustomObjectMapper)
                            .build();
    }
}
