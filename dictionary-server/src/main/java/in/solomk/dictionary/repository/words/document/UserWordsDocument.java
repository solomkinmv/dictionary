package in.solomk.dictionary.repository.words.document;

import in.solomk.dictionary.service.words.model.UserWords;
import in.solomk.dictionary.service.words.model.Word;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Document(collection = "user_words")
public record UserWordsDocument(
        @Id
        String documentId,
        Map<String, WordDocument> words
) {

    public static UserWordsDocument valueOf(String documentId, UserWords userWords) {
        return new UserWordsDocument(
                documentId,
                userWords.words()
                         .values()
                         .stream()
                        .map(WordDocument::valueOf)
                        .collect(Collectors.toMap(WordDocument::id, Function.identity()))
        );
    }

    public UserWords toModel() {
        return new UserWords(
                words.values()
                     .stream()
                     .map(WordDocument::toModel)
                     .collect(Collectors.toMap(Word::id, Function.identity()))
        );
    }
}
