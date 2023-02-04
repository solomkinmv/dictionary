package in.solomk.dictionary.api.handler.profile;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@Component
public class ProfileHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.principal()
                      .map(Principal::getName)
                      .flatMap(username -> ServerResponse.ok()
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .bodyValue(Map.of("message", "Hello " + username)));
    }
}
