package in.solomk.dictionary.service.user;

import reactor.core.publisher.Mono;

public interface UserProfileRepository {

    Mono<UserProfile> findByUserId(String userId);

    Mono<UserProfile> findBySocialProviderIds(String socialProviderId);

    Mono<UserProfile> createUser(String socialProviderId);
}
