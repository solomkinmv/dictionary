package in.solomk.dictionary.api.dto;

public record CreateWordRequest(
        String word,
        String translation
) {
}
