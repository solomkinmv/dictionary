package in.solomk.dictionary.repository.mongo;

import in.solomk.dictionary.repository.mongo.entity.UserWordsDocument;
import in.solomk.dictionary.service.UserWordsRepository;
import in.solomk.dictionary.service.model.UserWords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PersistentUserWordsRepository implements UserWordsRepository {

    private final ReactiveMongoUserWordsRepository repository;

    @Override
    public Mono<UserWords> saveUserWords(UserWords userWords) {
        return repository.save(UserWordsDocument.valueOf(userWords))
                         .map(UserWordsDocument::toModel);
    }

    @Override
    public Mono<UserWords> getUserWords(String userId) {
        return repository.findById(userId)
                         .map(UserWordsDocument::toModel);
    }
}
