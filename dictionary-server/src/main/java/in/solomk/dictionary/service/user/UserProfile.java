package in.solomk.dictionary.service.user;

import java.util.Set;

public record UserProfile(
        String id,
        String name,
        String email,
        Set<String> socialProviderIds
) {
}
