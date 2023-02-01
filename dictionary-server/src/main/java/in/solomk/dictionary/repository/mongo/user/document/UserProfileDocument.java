package in.solomk.dictionary.repository.mongo.user.document;

import java.util.List;
import java.util.Set;

public record UserProfileDocument(
        String id,
        String name,
        String email,
        Set<String> socialProviderIds,
        List<LearningLanguageContainer> languages
) {

    public static UserProfileDocument empty() {
        return new UserProfileDocument(null, null, null, null, null);
    }
}
