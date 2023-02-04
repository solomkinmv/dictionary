package in.solomk.dictionary.api.dto.words;

import java.util.Map;

public record UserWordsResponse(Map<String, WordResponse> words) {
}
