package in.solomk.dictionary.api.dto;

public record WordResponse(
        String id,
        String wordText,
        String translation
) {
}
