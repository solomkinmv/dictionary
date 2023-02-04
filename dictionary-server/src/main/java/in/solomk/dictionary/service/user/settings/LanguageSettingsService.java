package in.solomk.dictionary.service.user.settings;

import in.solomk.dictionary.service.user.language.SupportedLanguage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LanguageSettingsService {

    public List<SupportedLanguage> getSupportedLanguages() {
        return Arrays.asList(SupportedLanguage.values());
    }
}
