package in.solomk.dictionary.service.user.model;

import lombok.With;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public record UserProfile(
        String id,
        String name,
        String email,
        Set<String> socialProviderIds,
        @With
        List<LearningLanguage> languages
) {

    @Override
    public List<LearningLanguage> languages() {
        return languages == null ? Collections.emptyList() : languages;
    }
}
