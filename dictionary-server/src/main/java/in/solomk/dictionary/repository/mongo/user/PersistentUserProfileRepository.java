package in.solomk.dictionary.repository.mongo.user;

import in.solomk.dictionary.repository.mongo.user.entity.UserProfileDocument;
import in.solomk.dictionary.service.user.UserProfile;
import in.solomk.dictionary.service.user.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
@AllArgsConstructor
public class PersistentUserProfileRepository implements UserProfileRepository {

    private final ReactiveMongoUserProfileRepository reactiveMongoUserProfileRepository;

    @Override
    public Mono<UserProfile> findByUserId(String userId) {
        return reactiveMongoUserProfileRepository.findById(userId)
                .map(UserProfileDocument::toModel);
    }

    @Override
    public Mono<UserProfile> findBySocialProviderIds(String socialProviderId) {
        return reactiveMongoUserProfileRepository.findBySocialProviderIds(socialProviderId)
                .map(UserProfileDocument::toModel);
    }

    @Override
    public Mono<UserProfile> createUser(String socialProviderId) {
        return reactiveMongoUserProfileRepository.insert(new UserProfileDocument(null, null, null, Set.of(socialProviderId)))
                .map(UserProfileDocument::toModel);
    }
}
