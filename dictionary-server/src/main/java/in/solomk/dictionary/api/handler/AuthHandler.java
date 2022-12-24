package in.solomk.dictionary.api.handler;

import in.solomk.dictionary.api.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class AuthHandler implements HandlerFunction<ServerResponse> {

    private final TokenService tokenService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .doOnNext(authentication -> log.info("Token requested for user: {}", authentication.getName()))
                .map(tokenService::generateToken)
                .doOnNext(token -> log.info("Generated token: {}", token))
                .flatMap(token -> ServerResponse.ok()
                                                .bodyValue(token))
                .switchIfEmpty(ServerResponse.badRequest()
                                             .bodyValue("User not found"));
    }
}
