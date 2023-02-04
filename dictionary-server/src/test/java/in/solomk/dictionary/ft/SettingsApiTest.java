package in.solomk.dictionary.ft;

import in.solomk.dictionary.ft.client.SettingsTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SettingsApiTest extends BaseFuncTest {

    @Autowired
    private SettingsTestClient settingsTestClient;

    @Test
    void returnsSupportedLanguages() {
        settingsTestClient.getSupportedLanguages(userToken)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {
                                          "supportedLanguages": [
                                            {
                                              "languageCode": "en",
                                              "languageName": "English"
                                            },
                                            {
                                              "languageCode": "es",
                                              "languageName": "Español"
                                            },
                                            {
                                              "languageCode": "uk",
                                              "languageName": "Українська"
                                            }
                                          ]
                                        }""", true);
    }
}
