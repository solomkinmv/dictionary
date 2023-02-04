package in.solomk.dictionary.service.profile;

import in.solomk.dictionary.service.profile.model.UserProfile;
import reactor.core.publisher.Mono;

public interface UserProfileRepository {

    Mono<UserProfile> findByUserId(String userId);

    Mono<UserProfile> findBySocialProviderIds(String socialProviderId);

    Mono<UserProfile> createUser(String socialProviderId);

    Mono<UserProfile> save(UserProfile userProfile);
}
