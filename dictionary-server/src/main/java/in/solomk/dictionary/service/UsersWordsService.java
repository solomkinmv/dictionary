package in.solomk.dictionary.service;

import in.solomk.dictionary.service.model.UnsavedWord;
import in.solomk.dictionary.service.model.UserWords;
import in.solomk.dictionary.service.model.Word;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersWordsService {

    private final UserWordsRepository repository;

    public Mono<UserWords> getUserWords(String userId) {
        return repository.getUserWords(userId)
                         .switchIfEmpty(Mono.just(UserWords.of(userId)));
    }

    public Mono<Word> saveWord(String userId, UnsavedWord unsavedWord) {
        Word wordWithId = new Word(UUID.randomUUID().toString(), unsavedWord.word(), null, unsavedWord.translation());
        return repository.getUserWords(userId)
                         .switchIfEmpty(Mono.just(UserWords.of(userId)))
                         .map(userWords -> userWords.addWord(wordWithId))
                         .flatMap(repository::saveUserWords)
                         .thenReturn(wordWithId);
    }
}
