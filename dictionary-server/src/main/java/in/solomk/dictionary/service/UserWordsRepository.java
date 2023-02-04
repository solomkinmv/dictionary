package in.solomk.dictionary.service;

import in.solomk.dictionary.service.model.UserWords;
import in.solomk.dictionary.service.user.language.SupportedLanguage;
import reactor.core.publisher.Mono;

public interface UserWordsRepository {
    Mono<UserWords> saveUserWords(String userId, SupportedLanguage language, UserWords userWords);

    Mono<UserWords> getUserWords(String userId, SupportedLanguage language);
}
