package in.solomk.dictionary.repository.words;

import in.solomk.dictionary.repository.words.document.UserWordsDocument;
import in.solomk.dictionary.service.language.SupportedLanguage;
import in.solomk.dictionary.service.words.UserWordsRepository;
import in.solomk.dictionary.service.words.model.UserWords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PersistentUserWordsRepository implements UserWordsRepository {

    private final ReactiveMongoUserWordsRepository repository;

    @Override
    public Mono<UserWords> saveUserWords(String userId, SupportedLanguage language, UserWords userWords) {
        var documentId = generateDocumentId(userId, language);
        return repository.save(UserWordsDocument.valueOf(documentId, userWords))
                .map(UserWordsDocument::toModel);
    }

    @Override
    public Mono<UserWords> getUserWords(String userId, SupportedLanguage language) {
        return repository.findById(generateDocumentId(userId, language))
                .map(UserWordsDocument::toModel);
    }

    private String generateDocumentId(String userId, SupportedLanguage language) {
        return userId + "_" + language.getLanguageCode();
    }
}
