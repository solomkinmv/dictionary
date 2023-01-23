package in.solomk.dictionary.ft;

import in.solomk.dictionary.ft.client.ActuatorTestClient;
import org.junit.jupiter.api.Test;

import static in.solomk.dictionary.ft.client.ActuatorTestClient.AuthenticationMode.AUTHENTICATED;
import static in.solomk.dictionary.ft.client.ActuatorTestClient.AuthenticationMode.INVALID_CREDENTIALS;

public class ActuatorApiTest extends BaseFuncTest {

    @Test
    void returnsBasicHealthWhenNotAuthenticated() {
        actuatorTestClient.getHealth(ActuatorTestClient.AuthenticationMode.UNAUTHENTICATED)
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
        actuatorTestClient.getHealth(AUTHENTICATED)
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
        actuatorTestClient.getInfo(AUTHENTICATED)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {}
                                        """, true);
    }

    @Test
    void returnsUnauthorizedErrorForEnvEndpointIfNotAuthenticated() {
        actuatorTestClient.getEnv(ActuatorTestClient.AuthenticationMode.UNAUTHENTICATED)
                          .expectStatus()
                          .isUnauthorized();
    }

    @Test
    void returnsUnauthorizedErrorForMetricsIfCredentialsInvalid() {
        actuatorTestClient.getMetrics(INVALID_CREDENTIALS)
                          .expectStatus()
                          .isUnauthorized();
    }

    @Test
    void returnsListOfActuatorEndpoints() {
        actuatorTestClient.getAllActuatorEndpoints(AUTHENTICATED)
                          .expectStatus()
                          .isOk()
                          .expectBody()
                          .json("""
                                        {
                                          "_links": {
                                            "self": {
                                              "href": "/actuator",
                                              "templated": false
                                            },
                                            "beans": {
                                              "href": "/actuator/beans",
                                              "templated": false
                                            },
                                            "caches-cache": {
                                              "href": "/actuator/caches/{cache}",
                                              "templated": true
                                            },
                                            "caches": {
                                              "href": "/actuator/caches",
                                              "templated": false
                                            },
                                            "health-path": {
                                              "href": "/actuator/health/{*path}",
                                              "templated": true
                                            },
                                            "health": {
                                              "href": "/actuator/health",
                                              "templated": false
                                            },
                                            "info": {
                                              "href": "/actuator/info",
                                              "templated": false
                                            },
                                            "conditions": {
                                              "href": "/actuator/conditions",
                                              "templated": false
                                            },
                                            "configprops": {
                                              "href": "/actuator/configprops",
                                              "templated": false
                                            },
                                            "configprops-prefix": {
                                              "href": "/actuator/configprops/{prefix}",
                                              "templated": true
                                            },
                                            "env": {
                                              "href": "/actuator/env",
                                              "templated": false
                                            },
                                            "env-toMatch": {
                                              "href": "/actuator/env/{toMatch}",
                                              "templated": true
                                            },
                                            "loggers": {
                                              "href": "/actuator/loggers",
                                              "templated": false
                                            },
                                            "loggers-name": {
                                              "href": "/actuator/loggers/{name}",
                                              "templated": true
                                            },
                                            "heapdump": {
                                              "href": "/actuator/heapdump",
                                              "templated": false
                                            },
                                            "threaddump": {
                                              "href": "/actuator/threaddump",
                                              "templated": false
                                            },
                                            "metrics": {
                                              "href": "/actuator/metrics",
                                              "templated": false
                                            },
                                            "metrics-requiredMetricName": {
                                              "href": "/actuator/metrics/{requiredMetricName}",
                                              "templated": true
                                            },
                                            "scheduledtasks": {
                                              "href": "/actuator/scheduledtasks",
                                              "templated": false
                                            },
                                            "mappings": {
                                              "href": "/actuator/mappings",
                                              "templated": false
                                            }
                                          }
                                        }""", true);
    }
}
