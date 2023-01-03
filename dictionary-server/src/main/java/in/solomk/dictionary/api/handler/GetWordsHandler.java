package in.solomk.dictionary.api.handler;

import in.solomk.dictionary.api.dto.UserWordsResponse;
import in.solomk.dictionary.api.mapper.UserWordsWebApiMapper;
import in.solomk.dictionary.service.UsersWordsService;
import lombok.AllArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
@AllArgsConstructor
public class GetWordsHandler implements HandlerFunction<ServerResponse> {

    private final UsersWordsService usersWordsService;
    private final UserWordsWebApiMapper mapper;

    @Override
    @RegisterReflectionForBinding(value = UserWordsResponse.class)
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.principal()
                .map(Principal::getName)
                .flatMap(usersWordsService::getUserWords)
                .map(mapper::toUserWordsResponse)
                .flatMap(userWordsResponse -> ServerResponse.ok()
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .bodyValue(userWordsResponse))
                .switchIfEmpty(ServerResponse.notFound()
                                             .build());
    }
}
