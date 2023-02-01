package in.solomk.dictionary.api.mapper;

import in.solomk.dictionary.api.dto.language.LearningLanguageResponse;
import in.solomk.dictionary.api.dto.language.LearningLanguagesAggregatedResponse;
import in.solomk.dictionary.service.user.model.LearningLanguageWithName;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LearningLanguagesWebApiMapper {

    List<LearningLanguageResponse> toLearningLanguageResponse(List<LearningLanguageWithName> languages);

    default LearningLanguagesAggregatedResponse toLearningLanguagesAggregatedResponse(List<LearningLanguageWithName> languages) {
        return new LearningLanguagesAggregatedResponse(toLearningLanguageResponse(languages));
    }
}
