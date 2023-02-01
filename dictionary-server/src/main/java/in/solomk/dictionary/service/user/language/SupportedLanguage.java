package in.solomk.dictionary.service.user.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SupportedLanguage {
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    ;

    private final String languageCode;
    private final String languageName;

    private static final Map<String, SupportedLanguage> supportedLanguageIndex = new HashMap<>();

    static {
        for (SupportedLanguage supportedLanguage : SupportedLanguage.values()) {
            supportedLanguageIndex.put(supportedLanguage.languageCode, supportedLanguage);
        }
    }

    public static Optional<SupportedLanguage> getByLanguageCode(String languageCode) {
        return Optional.ofNullable(supportedLanguageIndex.get(languageCode));
    }
}
