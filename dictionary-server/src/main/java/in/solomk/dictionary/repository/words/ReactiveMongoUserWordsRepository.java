package in.solomk.dictionary.repository.words;

import in.solomk.dictionary.repository.words.document.UserWordsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveMongoUserWordsRepository extends ReactiveMongoRepository<UserWordsDocument, String> {
}
