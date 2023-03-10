package in.solomk.dictionary.service.words.model;

import java.util.HashMap;
import java.util.Map;

public record UserWords(Map<String, Word> words) {

    public static final UserWords EMPTY = new UserWords(Map.of());

    public UserWords deleteWord(String wordId) {
        var newWords = new HashMap<>(words);
        newWords.remove(wordId);
        return new UserWords(newWords);
    }

    public UserWords addWord(Word word) {
        var newWords = new HashMap<>(words);
        newWords.put(word.id(), word);
        return new UserWords(newWords);
    }
}
