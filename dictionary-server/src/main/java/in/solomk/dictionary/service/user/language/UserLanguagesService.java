package in.solomk.dictionary.service.user.language;

import in.solomk.dictionary.service.exception.AlreadyExistingException;
import in.solomk.dictionary.service.user.UserProfileRepository;
import in.solomk.dictionary.service.user.model.LearningLanguage;
import in.solomk.dictionary.service.user.model.LearningLanguageWithName;
import in.solomk.dictionary.service.user.model.UserProfile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserLanguagesService {
    private final UserProfileRepository userProfileRepository;

    public Mono<List<LearningLanguageWithName>> getLearningLanguagesByUserId(String userId) {
        return userProfileRepository.findByUserId(userId)
                .map(UserProfile::languages)
                .map(this::enrichLearningLanguagesWithFullNames);
    }

    public Mono<List<LearningLanguageWithName>> createLearningLanguage(String userId, String newLearningLanguage) {
        return userProfileRepository.findByUserId(userId)
                                    .flatMap(userProfile -> addLanguage(newLearningLanguage, userProfile))
                                    .flatMap(userProfileRepository::save)
                .map(UserProfile::languages)
                .map(this::enrichLearningLanguagesWithFullNames);
    }

    public Mono<List<LearningLanguageWithName>> deleteLearningLanguage(String userId, String languageCode) {
        return userProfileRepository.findByUserId(userId)
                                    .flatMap(userProfile -> removeLanguage(languageCode, userProfile))
                                    .flatMap(userProfileRepository::save)
                .map(UserProfile::languages)
                .map(this::enrichLearningLanguagesWithFullNames);
    }

    private Mono<UserProfile> removeLanguage(String languageCode, UserProfile userProfile) {
        var learningLanguage = new LearningLanguage(languageCode);
        if (!userProfile.languages().contains(learningLanguage)) {
            return Mono.just(userProfile);
        }
        var updatedLearningLanguages = new ArrayList<>(userProfile.languages());
        updatedLearningLanguages.remove(learningLanguage);
        return Mono.just(userProfile.withLanguages(updatedLearningLanguages));
    }

    private List<LearningLanguageWithName> enrichLearningLanguagesWithFullNames(List<LearningLanguage> learningLanguages) {
        return learningLanguages.stream()
                .map(this::toLearningLanguageWithName)
                .toList();
    }

    private LearningLanguageWithName toLearningLanguageWithName(LearningLanguage learningLanguage) {
        return new LearningLanguageWithName(learningLanguage.languageCode(),
                                            SupportedLanguage.getByLanguageCode(learningLanguage.languageCode())
                                                    .map(SupportedLanguage::getLanguageName)
                                                    .orElse("Unknown"));
    }

    private Mono<UserProfile> addLanguage(String newLearningLanguageCode, UserProfile userProfile) {
        var newLearningLanguage = new LearningLanguage(newLearningLanguageCode);
        if (userProfile.languages().contains(newLearningLanguage)) {
            return Mono.error(new AlreadyExistingException("Language %s already exists", newLearningLanguageCode));
        }
        var updatedLearningLanguages = new ArrayList<>(userProfile.languages());
        updatedLearningLanguages.add(newLearningLanguage);
        return Mono.just(userProfile.withLanguages(updatedLearningLanguages));
    }

}
