package in.solomk.dictionary.api.mapper;

import in.solomk.dictionary.api.dto.CreateWordRequest;
import in.solomk.dictionary.api.dto.UserWordsResponse;
import in.solomk.dictionary.api.dto.WordResponse;
import in.solomk.dictionary.service.model.UnsavedWord;
import in.solomk.dictionary.service.model.UserWords;
import in.solomk.dictionary.service.model.Word;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserWordsWebApiMapper {

    UserWordsResponse toUserWordsResponse(UserWords userWordsDocument);

    UnsavedWord toUnsavedWord(CreateWordRequest createWordRequest);

    WordResponse toWordResponse(Word word);
}
