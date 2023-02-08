package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.dto.words.CreateWordRequest;
import in.solomk.dictionary.api.dto.words.UserWordsResponse;
import in.solomk.dictionary.api.dto.words.WordResponse;
import in.solomk.dictionary.service.language.SupportedLanguage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static in.solomk.dictionary.service.language.SupportedLanguage.ENGLISH;
import static in.solomk.dictionary.service.language.SupportedLanguage.UKRAINIAN;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class WordsApiTest extends BaseFuncTest {

    @Test
    void returnsEmptyUserWords() {
        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(emptyMap()));
    }

    @Test
    void addsWordForUser() {
        userLanguagesTestClient.addLanguage(userToken, ENGLISH.getLanguageCode());
        var request = new CreateWordRequest("word-1", "meaning-1");
        WordResponse wordResponse = userWordsTestClient.addWord(userToken, ENGLISH.getLanguageCode(), request)
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

        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(Map.of(wordResponse.id(), wordResponse)));
    }

    @Test
    void returnsBadRequestIfLanguageIsNotSupported() {
        var request = new CreateWordRequest("word-1", "meaning-1");
        userWordsTestClient.addWord(userToken, "xxx", request)
                           .expectStatus()
                           .isBadRequest()
                           .expectBody()
                           .json("""
                                         {
                                           "path": "/api/languages/xxx/words",
                                           "status": 400,
                                           "error": "Bad Request",
                                           "message": "Language code is not supported"
                                         }""")
                           .jsonPath("$.requestId").isNotEmpty();
    }

    @Test
    void addsWordsFromDifferentLanguages() {
        userLanguagesTestClient.addLanguage(userToken, ENGLISH.getLanguageCode());
        WordResponse wordResponse = userWordsTestClient.addWord(userToken, ENGLISH.getLanguageCode(),
                                                                new CreateWordRequest("word-1", "meaning-1"))
                                                       .expectStatus().isOk()
                                                       .expectBody(WordResponse.class)
                                                       .returnResult()
                                                       .getResponseBody();
        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(Map.of(wordResponse.id(), wordResponse)));

        userLanguagesTestClient.addLanguage(userToken, UKRAINIAN.getLanguageCode());
        WordResponse wordResponse2 = userWordsTestClient.addWord(userToken, UKRAINIAN.getLanguageCode(),
                                                                 new CreateWordRequest("слава", "glory"))
                                                        .expectStatus().isOk()
                                                        .expectBody(WordResponse.class)
                                                        .returnResult()
                                                        .getResponseBody();

        verifyUserWordsResponse(UKRAINIAN, new UserWordsResponse(Map.of(wordResponse2.id(), wordResponse2)));
    }

    @Test
    void returnsBadRequestIfAddingWordForNotStudiedLanguage() {
        userWordsTestClient.addWord(userToken, UKRAINIAN.getLanguageCode(),
                                    new CreateWordRequest("слава", "glory"))
                           .expectStatus()
                           .isBadRequest()
                           .expectBody()
                           .json("""
                                         {
                                           "path": "/api/languages/uk/words",
                                           "status": 400,
                                           "error": "Bad Request",
                                           "message": "Language is not studied. Language code: uk"
                                         }""")
                           .jsonPath("$.requestId").isNotEmpty();
    }

    @Test
    void deletesWordById() {
        userLanguagesTestClient.addLanguage(userToken, ENGLISH.getLanguageCode());
        WordResponse wordResponse1 = userWordsTestClient.addWord(userToken, ENGLISH.getLanguageCode(),
                                                                 new CreateWordRequest("word-1", "meaning-1"))
                                                        .expectBody(WordResponse.class)
                                                        .returnResult()
                                                        .getResponseBody();
        WordResponse wordResponse2 = userWordsTestClient.addWord(userToken, ENGLISH.getLanguageCode(),
                                                                 new CreateWordRequest("word-2", "meaning-2"))
                                                        .expectBody(WordResponse.class)
                                                        .returnResult()
                                                        .getResponseBody();
        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(Map.of(wordResponse1.id(), wordResponse1, wordResponse2.id(), wordResponse2)));

        userWordsTestClient.deleteWord(userToken, ENGLISH.getLanguageCode(), wordResponse1.id())
                           .expectStatus()
                           .isOk();
        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(Map.of(wordResponse2.id(), wordResponse2)));

        userWordsTestClient.deleteWord(userToken, ENGLISH.getLanguageCode(), wordResponse2.id())
                           .expectStatus()
                           .isOk();
        verifyUserWordsResponse(ENGLISH, new UserWordsResponse(emptyMap()));
    }

    private void verifyUserWordsResponse(SupportedLanguage language, UserWordsResponse expectedValue) {
        userWordsTestClient.getUserWords(userToken, language.getLanguageCode())
                           .expectStatus()
                           .isOk()
                           .expectBody(UserWordsResponse.class)
                           .isEqualTo(expectedValue);
    }

}
