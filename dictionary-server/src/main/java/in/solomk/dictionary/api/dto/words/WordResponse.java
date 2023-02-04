package in.solomk.dictionary.api.dto.words;

public record WordResponse(
        String id,
        String wordText,
        String translation
) {
}
