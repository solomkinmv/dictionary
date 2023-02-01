package in.solomk.dictionary.api.handler.language;

import in.solomk.dictionary.api.dto.WordResponse;
import in.solomk.dictionary.api.dto.language.LearningLanguagesAggregatedResponse;
import in.solomk.dictionary.api.mapper.LearningLanguagesWebApiMapper;
import in.solomk.dictionary.service.user.language.UserLanguagesService;
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
public class DeleteLanguageHandler implements HandlerFunction<ServerResponse> {

    private final UserLanguagesService userLanguagesService;
    private final LearningLanguagesWebApiMapper mapper;

    @Override
    @RegisterReflectionForBinding(value = WordResponse.class)
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.principal()
                .map(Principal::getName)
                .flatMap(userId -> ServerResponse.ok()
                                                 .contentType(APPLICATION_JSON)
                                                 .body(deleteLanguage(request, userId),
                                                       LearningLanguagesAggregatedResponse.class));
    }

    private Mono<LearningLanguagesAggregatedResponse> deleteLanguage(ServerRequest request, String userId) {
        var languageCode = request.pathVariable("languageCode");
        return userLanguagesService.deleteLearningLanguage(userId, languageCode)
                .map(mapper::toLearningLanguagesAggregatedResponse);
    }

}
