package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.dto.language.LearningLanguageResponse;
import in.solomk.dictionary.api.dto.language.LearningLanguagesAggregatedResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class LanguagesApiTest extends BaseFuncTest {

    @Test
    void returnsEmptyUserLanguages() {
        verifyUserLanguagesResponse(new LearningLanguagesAggregatedResponse(emptyList()));
    }

    @Test
    void addsLanguageForUser() {
        LearningLanguagesAggregatedResponse expectedResponse = new LearningLanguagesAggregatedResponse(List.of(new LearningLanguageResponse("en", "English")));

        var userLanguagesResponse = userLanguagesTestClient.addLanguage(userToken, "en")
                                                           .expectStatus().isOk()
                                                           .expectBody(LearningLanguagesAggregatedResponse.class)
                                                           .returnResult()
                                                           .getResponseBody();

        assertThat(userLanguagesResponse).isEqualTo(expectedResponse);

        verifyUserLanguagesResponse(expectedResponse);
    }

    private void verifyUserLanguagesResponse(LearningLanguagesAggregatedResponse expectedValue) {
        userLanguagesTestClient.getLanguages(userToken)
                               .expectStatus()
                               .isOk()
                               .expectBody(LearningLanguagesAggregatedResponse.class)
                               .isEqualTo(expectedValue);
    }
}
