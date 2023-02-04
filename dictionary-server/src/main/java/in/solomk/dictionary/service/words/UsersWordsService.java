package in.solomk.dictionary.service.words;

import in.solomk.dictionary.service.language.SupportedLanguage;
import in.solomk.dictionary.service.words.model.UnsavedWord;
import in.solomk.dictionary.service.words.model.UserWords;
import in.solomk.dictionary.service.words.model.Word;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersWordsService {

    private final UserWordsRepository repository;

    public Mono<UserWords> getUserWords(String userId, SupportedLanguage language) {
        return repository.getUserWords(userId, language)
                         .switchIfEmpty(Mono.just(UserWords.EMPTY));
    }

    public Mono<Word> saveWord(String userId, SupportedLanguage language, UnsavedWord unsavedWord) {
        Word wordWithId = new Word(UUID.randomUUID().toString(), unsavedWord.wordText(), null, unsavedWord.translation());
        return repository.getUserWords(userId, language)
                         .switchIfEmpty(Mono.just(UserWords.EMPTY))
                .map(userWords -> userWords.addWord(wordWithId))
                .flatMap(userWords -> repository.saveUserWords(userId, language, userWords))
                .thenReturn(wordWithId);
    }
}
