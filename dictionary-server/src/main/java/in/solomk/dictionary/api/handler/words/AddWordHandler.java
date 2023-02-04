package in.solomk.dictionary.api.handler.words;

import in.solomk.dictionary.api.dto.words.CreateWordRequest;
import in.solomk.dictionary.api.dto.words.WordResponse;
import in.solomk.dictionary.api.mapper.UserWordsWebApiMapper;
import in.solomk.dictionary.exception.BadRequestException;
import in.solomk.dictionary.service.language.SupportedLanguage;
import in.solomk.dictionary.service.words.UsersWordsService;
import lombok.AllArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
@Component
public class AddWordHandler implements HandlerFunction<ServerResponse> {

    private final UsersWordsService usersWordsService;
    private final UserWordsWebApiMapper mapper;

    @Override
    @RegisterReflectionForBinding(value = WordResponse.class)
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.principal()
                .map(Principal::getName)
                .flatMap(userId -> ServerResponse.ok()
                                                 .contentType(APPLICATION_JSON)
                                                 .body(addWord(request, userId),
                                                       WordResponse.class));
    }

    private Mono<WordResponse> addWord(ServerRequest request, String userId) {
        return extractRequestBody(request)
                .flatMap(createWordRequest -> usersWordsService.saveWord(userId,
                                                                         extractLanguageCode(request),
                                                                         mapper.toUnsavedWord(createWordRequest)))
                .map(mapper::toWordResponse);
    }

    private Mono<CreateWordRequest> extractRequestBody(ServerRequest request) {
        return request.bodyToMono(CreateWordRequest.class);
    }

    private SupportedLanguage extractLanguageCode(ServerRequest request) {
        var languageCode = request.pathVariable("languageCode");
        return SupportedLanguage.getByLanguageCode(languageCode)
                                .orElseThrow(() -> new BadRequestException("Language code is not supported", languageCode));
    }

}
