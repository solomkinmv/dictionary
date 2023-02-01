package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.dto.CreateWordRequest;
import in.solomk.dictionary.api.dto.UserWordsResponse;
import in.solomk.dictionary.api.dto.WordResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class WordsApiTest extends BaseFuncTest {

    @Test
    void returnsEmptyUserWords() {
        verifyUserWordsResponse(new UserWordsResponse(userId, emptyMap()));
    }

    @Test
    void addsWordForUser() {
        WordResponse wordResponse = userWordsTestClient.addWord(userToken, new CreateWordRequest("word-1", "meaning-1"))
                                                       .expectStatus().isOk()
                                                       .expectBody(WordResponse.class)
                                                       .returnResult()
                                                       .getResponseBody();

        assertThat(wordResponse).isNotNull();
        assertThat(wordResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new WordResponse(null, "word-1", "meaning-1"));
        assertThat(wordResponse.id()).isNotBlank();

        verifyUserWordsResponse(new UserWordsResponse(userId, Map.of(wordResponse.id(), wordResponse)));
    }

    private void verifyUserWordsResponse(UserWordsResponse expectedValue) {
        userWordsTestClient.getUserWords(userToken)
                           .expectStatus()
                           .isOk()
                           .expectBody(UserWordsResponse.class)
                           .isEqualTo(expectedValue);
    }

}
