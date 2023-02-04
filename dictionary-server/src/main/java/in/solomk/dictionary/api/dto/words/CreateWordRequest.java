package in.solomk.dictionary.api.dto.words;

public record CreateWordRequest(
        String wordText,
        String translation
) {
}
