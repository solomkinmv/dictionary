package in.solomk.dictionary.service.words;

import in.solomk.dictionary.service.language.SupportedLanguage;
import in.solomk.dictionary.service.words.model.UserWords;
import reactor.core.publisher.Mono;

public interface UserWordsRepository {
    Mono<UserWords> saveUserWords(String userId, SupportedLanguage language, UserWords userWords);

    Mono<UserWords> getUserWords(String userId, SupportedLanguage language);
}
