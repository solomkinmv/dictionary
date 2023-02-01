package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.ft.client.ActuatorTestClient;
import in.solomk.dictionary.ft.client.UserLanguagesTestClient;
import in.solomk.dictionary.ft.client.UserWordsTestClient;
import in.solomk.dictionary.service.user.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

@SpringBootTest
@ActiveProfiles({"test"})
public class BaseFuncTest {

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    static {
        mongoDBContainer.start();
    }

    @Autowired
    protected UserLanguagesTestClient userLanguagesTestClient;

    @Autowired
    protected UserWordsTestClient userWordsTestClient;
    protected String userId;
    @Autowired
    protected ActuatorTestClient actuatorTestClient;
    protected String userToken;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserProfileService userProfileService;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    }

    protected String generateId() {
        return String.valueOf(idGenerator.getAndIncrement());
    }

    @BeforeEach
    void setUpUserId() {
        userId = requireNonNull(
                userProfileService.getOrCreateUserProfileBySocialProviderId("google", generateId())
                                  .block())
                .id();
        userToken = tokenService.generateToken(userId);
    }
}
