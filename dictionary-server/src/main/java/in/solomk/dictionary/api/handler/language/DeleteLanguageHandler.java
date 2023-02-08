package in.solomk.dictionary.api.handler.language;

import in.solomk.dictionary.api.dto.language.LearningLanguagesAggregatedResponse;
import in.solomk.dictionary.api.dto.words.WordResponse;
import in.solomk.dictionary.api.mapper.LearningLanguagesWebApiMapper;
import in.solomk.dictionary.exception.BadRequestException;
import in.solomk.dictionary.service.language.SupportedLanguage;
import in.solomk.dictionary.service.language.UserLanguagesService;
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
public class DeleteLanguageHandler implements HandlerFunction<ServerResponse> {

    private final UserLanguagesService userLanguagesService;
    private final UsersWordsService usersWordsService;
    private final LearningLanguagesWebApiMapper mapper;

    @Override
    @RegisterReflectionForBinding(value = WordResponse.class)
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.principal()
                .map(Principal::getName)
                .flatMap(userId -> ServerResponse.ok()
                                                 .contentType(APPLICATION_JSON)
                                                 .body(deleteLanguageAndWords(request, userId),
                                                       LearningLanguagesAggregatedResponse.class));
    }

    private Mono<LearningLanguagesAggregatedResponse> deleteLanguageAndWords(ServerRequest request, String userId) {
        var supportedLanguage = getSafeLanguage(request.pathVariable("languageCode"));
        return usersWordsService.deleteAllUserWords(userId, supportedLanguage)
                                .then(userLanguagesService.deleteLearningLanguage(userId, supportedLanguage))
                .map(mapper::toLearningLanguagesAggregatedResponse);
    }


    private SupportedLanguage getSafeLanguage(String languageCode) {
        return SupportedLanguage.getByLanguageCode(languageCode)
                                .orElseThrow(() -> new BadRequestException("Language code is not supported %s", languageCode));
    }

}
