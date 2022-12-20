package in.solomk.dictionary.service.model;

import java.util.HashMap;
import java.util.Map;

public record UserWords(String userId,
                        Map<String, Word> words) {

    public static UserWords of(String userId) {
        return new UserWords(userId, Map.of());
    }

    public UserWords addWord(Word word) {
        var newWords = new HashMap<>(words);
        newWords.put(word.id(), word);
        return new UserWords(userId, newWords);
    }
}
