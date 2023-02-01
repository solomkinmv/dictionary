package in.solomk.dictionary.repository.mongo.user;

import in.solomk.dictionary.repository.mongo.user.document.UserProfileDocument;
import in.solomk.dictionary.service.user.UserProfileRepository;
import in.solomk.dictionary.service.user.model.UserProfile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
@AllArgsConstructor
public class PersistentUserProfileRepository implements UserProfileRepository {

    private final ReactiveMongoUserProfileRepository reactiveMongoUserProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public Mono<UserProfile> findByUserId(String userId) {
        return reactiveMongoUserProfileRepository.findById(userId)
                .map(userProfileMapper::toModel);
    }

    @Override
    public Mono<UserProfile> findBySocialProviderIds(String socialProviderId) {
        return reactiveMongoUserProfileRepository.findBySocialProviderIds(socialProviderId)
                .map(userProfileMapper::toModel);
    }

    @Override
    public Mono<UserProfile> createUser(String socialProviderId) {
        return reactiveMongoUserProfileRepository.insert(new UserProfileDocument(null, null, null, Set.of(socialProviderId), null))
                .map(userProfileMapper::toModel);
    }

    @Override
    public Mono<UserProfile> save(UserProfile userProfile) {
        return reactiveMongoUserProfileRepository.save(userProfileMapper.toDocument(userProfile))
                .map(userProfileMapper::toModel);
    }
}
