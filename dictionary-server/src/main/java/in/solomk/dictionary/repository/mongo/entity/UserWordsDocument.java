package in.solomk.dictionary.repository.mongo.entity;

import in.solomk.dictionary.service.model.UserWords;
import in.solomk.dictionary.service.model.Word;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Document(collection = "user_words")
public record UserWordsDocument(
        @Id
        String userId,
        Map<String, WordDocument> words
) {

    public static UserWordsDocument valueOf(UserWords userWords) {
        return new UserWordsDocument(
                userWords.userId(),
                userWords.words()
                         .values()
                         .stream()
                         .map(WordDocument::valueOf)
                         .collect(Collectors.toMap(WordDocument::id, Function.identity()))
        );
    }

    public UserWords toModel() {
        return new UserWords(
                userId,
                words.values()
                     .stream()
                     .map(WordDocument::toModel)
                     .collect(Collectors.toMap(Word::id, Function.identity()))
        );
    }
}
