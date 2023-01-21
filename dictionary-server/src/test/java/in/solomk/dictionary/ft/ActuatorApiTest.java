package in.solomk.dictionary.ft;

import org.junit.jupiter.api.Test;

public class ActuatorApiTest extends BaseFuncTest {

    @Test
    void returnsBasicHealthWhenNotAuthenticated() {
        actuatorTestClient.getHealth(false)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {
                                          "status": "UP"
                                        }""", true);
    }

    @Test
    void returnsDetailedHealthWhenAuthenticated() {
        actuatorTestClient.getHealth(true)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {
                                          "status": "UP",
                                          "components": {
                                            "diskSpace": {
                                              "status": "UP",
                                              "details": {
                                                "exists": true
                                              }
                                            },
                                            "mongo": {
                                              "status": "UP",
                                              "details": {
                                                "maxWireVersion": 13
                                              }
                                            },
                                            "ping": {
                                              "status": "UP"
                                            }
                                          }
                                        }""", false);
    }

    @Test
    void returnsInfo() {
        actuatorTestClient.getInfo(true)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {}
                                        """, true);
    }
}
