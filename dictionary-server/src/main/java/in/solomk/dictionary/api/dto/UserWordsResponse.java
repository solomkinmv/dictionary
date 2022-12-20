package in.solomk.dictionary.api.dto;

import java.util.Map;

public record UserWordsResponse(
        String userId,
        Map<String, WordResponse> words
) {
}
