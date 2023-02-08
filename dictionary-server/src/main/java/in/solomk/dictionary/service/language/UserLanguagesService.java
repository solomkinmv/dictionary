package in.solomk.dictionary.service.language;

import in.solomk.dictionary.exception.AlreadyExistingException;
import in.solomk.dictionary.exception.BadRequestException;
import in.solomk.dictionary.service.profile.UserProfileRepository;
import in.solomk.dictionary.service.profile.model.LearningLanguage;
import in.solomk.dictionary.service.profile.model.LearningLanguageWithName;
import in.solomk.dictionary.service.profile.model.UserProfile;
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

    public Mono<List<LearningLanguageWithName>> deleteLearningLanguage(String userId, SupportedLanguage supportedLanguage) {
        return userProfileRepository.findByUserId(userId)
                                    .flatMap(userProfile -> removeLanguage(supportedLanguage, userProfile))
                                    .flatMap(userProfileRepository::save)
                .map(UserProfile::languages)
                .map(this::enrichLearningLanguagesWithFullNames);
    }

    public Mono<Void> validateLanguageIsStudied(String userId, SupportedLanguage language) {
        return getLearningLanguagesByUserId(userId)
                .filter(languages ->
                                languages.stream()
                                         .anyMatch(learningLanguage -> languageCodesMatch(language, learningLanguage)))
                .switchIfEmpty(Mono.error(new BadRequestException("Language is not studied. Language code: %s",
                                                                  language.getLanguageCode())))
                .then();
    }

    private Mono<UserProfile> removeLanguage(SupportedLanguage supportedLanguage, UserProfile userProfile) {
        var learningLanguage = new LearningLanguage(supportedLanguage.getLanguageCode());
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

    private boolean languageCodesMatch(SupportedLanguage language, LearningLanguageWithName learningLanguage) {
        return learningLanguage.languageCode().equals(language.getLanguageCode());
    }

}
