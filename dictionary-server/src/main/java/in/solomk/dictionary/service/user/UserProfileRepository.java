package in.solomk.dictionary.service.user;

import in.solomk.dictionary.service.user.model.UserProfile;
import reactor.core.publisher.Mono;

public interface UserProfileRepository {

    Mono<UserProfile> findByUserId(String userId);

    Mono<UserProfile> findBySocialProviderIds(String socialProviderId);

    Mono<UserProfile> createUser(String socialProviderId);

    Mono<UserProfile> save(UserProfile userProfile);
}
