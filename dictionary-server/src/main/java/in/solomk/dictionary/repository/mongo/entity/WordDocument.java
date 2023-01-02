package in.solomk.dictionary.repository.mongo.entity;

import in.solomk.dictionary.service.model.Word;

public record WordDocument(
        String id,
        String wordText,
        String meaning,
        String translation
) {

    public static WordDocument valueOf(Word word) {
        return new WordDocument(word.id(), word.wordText(), word.meaning(), word.translation());
    }

    public Word toModel() {
        return new Word(id, wordText, meaning, translation);
    }
}
