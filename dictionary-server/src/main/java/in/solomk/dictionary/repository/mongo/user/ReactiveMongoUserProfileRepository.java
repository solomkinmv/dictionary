package in.solomk.dictionary.repository.mongo.user;

import in.solomk.dictionary.repository.mongo.user.document.UserProfileDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveMongoUserProfileRepository extends ReactiveMongoRepository<UserProfileDocument, String> {

    Mono<UserProfileDocument> findBySocialProviderIds(String socialProviderId);
}
