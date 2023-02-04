package in.solomk.dictionary.api.dto.settings;

import java.util.List;

public record LanguageSettingsResponse(List<SupportedLanguageResponse> supportedLanguages) {
}
