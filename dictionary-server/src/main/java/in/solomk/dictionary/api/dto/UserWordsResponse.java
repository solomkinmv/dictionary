package in.solomk.dictionary.api.dto;

import java.util.Map;

public record UserWordsResponse(Map<String, WordResponse> words) {
}
