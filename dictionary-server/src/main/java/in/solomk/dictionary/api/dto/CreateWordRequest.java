package in.solomk.dictionary.api.dto;

public record CreateWordRequest(
        String wordText,
        String translation
) {
}
