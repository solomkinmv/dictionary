package in.solomk.dictionary.api.handler;

import in.solomk.dictionary.api.dto.CreateWordRequest;
import in.solomk.dictionary.api.dto.WordResponse;
import in.solomk.dictionary.api.mapper.UserWordsWebApiMapper;
import in.solomk.dictionary.service.UsersWordsService;
import lombok.AllArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
@Component
public class AddWordHandler implements HandlerFunction<ServerResponse> {

    private final UsersWordsService usersWordsService;
    private final UserWordsWebApiMapper mapper;

    @Override
    @RegisterReflectionForBinding(value = WordResponse.class)
    public Mono<ServerResponse> handle(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return ServerResponse.ok()
                             .contentType(APPLICATION_JSON)
                             .body(addWord(request, userId),
                                   WordResponse.class);
    }

    private Mono<WordResponse> addWord(ServerRequest request, String userId) {
        return extractRequestBody(request)
                .flatMap(createWordRequest -> usersWordsService.saveWord(userId, mapper.toUnsavedWord(createWordRequest)))
                .map(mapper::toWordResponse);
    }

    private Mono<CreateWordRequest> extractRequestBody(ServerRequest request) {
        return request.bodyToMono(CreateWordRequest.class);
    }

}
