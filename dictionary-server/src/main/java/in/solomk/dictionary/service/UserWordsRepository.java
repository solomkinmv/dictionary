package in.solomk.dictionary.service;

import in.solomk.dictionary.service.model.UserWords;
import reactor.core.publisher.Mono;

public interface UserWordsRepository {
    Mono<UserWords> saveUserWords(UserWords userWords);

    Mono<UserWords> getUserWords(String userId);
}
