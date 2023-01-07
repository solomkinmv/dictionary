package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.security.TokenService;
import in.solomk.dictionary.ft.client.UserWordsTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@ActiveProfiles({"test"})
public class BaseFuncTest {

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    static {
        mongoDBContainer.start();
    }

    @Autowired
    protected UserWordsTestClient testClient;
    @Autowired
    protected TokenService tokenService;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    }

    protected String generateId() {
        return String.valueOf(idGenerator.getAndIncrement());
    }
}
