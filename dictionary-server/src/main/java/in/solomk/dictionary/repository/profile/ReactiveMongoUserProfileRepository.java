package in.solomk.dictionary.repository.profile;

import in.solomk.dictionary.repository.profile.document.UserProfileDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveMongoUserProfileRepository extends ReactiveMongoRepository<UserProfileDocument, String> {

    Mono<UserProfileDocument> findBySocialProviderIds(String socialProviderId);
}
