package in.solomk.dictionary.repository.mongo.user.entity;

import in.solomk.dictionary.service.user.UserProfile;

import java.util.Set;

public record UserProfileDocument(
        String id,
        String name,
        String email,
        Set<String> socialProviderIds
) {

    public static UserProfileDocument empty() {
        return new UserProfileDocument(null, null, null, null);
    }

    public UserProfile toModel() {
        return new UserProfile(
                id,
                name,
                email,
                socialProviderIds
        );
    }
}
