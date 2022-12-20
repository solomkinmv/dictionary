package in.solomk.dictionary.repository.mongo;

import in.solomk.dictionary.repository.mongo.entity.UserWordsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveMongoUserWordsRepository extends ReactiveMongoRepository<UserWordsDocument, String> {
}
