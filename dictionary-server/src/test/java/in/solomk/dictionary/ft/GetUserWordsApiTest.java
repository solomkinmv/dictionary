package in.solomk.dictionary.ft;

import in.solomk.dictionary.api.dto.CreateWordRequest;
import in.solomk.dictionary.api.dto.UserWordsResponse;
import in.solomk.dictionary.api.dto.WordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class GetUserWordsApiTest extends BaseFuncTest {

    private String userId;

    @BeforeEach
    void setUp() {
        userId = generateId();
    }

    @Test
    void returnsEmptyUserWords() {
        verifyUserWordsResponse(new UserWordsResponse(userId, emptyMap()));
    }

    @Test
    void addsWordForUser() {
        WordResponse wordResponse = testClient.addWord(userId, new CreateWordRequest("word-1", "meaning-1"))
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

//    @Test
//    void returnsUpdatedMeetingInformationForSingleUser() {
//        String meetingId = createUserWords();
//        var username = "user-1";
//        testClient.setIntervalsForUser(meetingId, username, List.of(intervalReq(100, 300), intervalReq(500, 1000)));
//
//        var singleUserIntervalResponses = List.of(intervalRes(100, 300), intervalRes(500, 1000));
//        verifyUserWordsResponse(
//                new MeetingResponse(meetingId,
//                                    Map.of(username, singleUserIntervalResponses),
//                                    singleUserIntervalResponses));
//    }
//
//    @Test
//    void returnsUpdatedMeetingInformationForMultipleUsersWithIntersections() {
//        String meetingId = createUserWords();
//        String user1 = "user-1", user2 = "user-2", user3 = "user-3";
//        testClient.setIntervalsForUser(meetingId, user1, List.of(intervalReq(100, 300), intervalReq(500, 1000)));
//        testClient.setIntervalsForUser(meetingId, user2, List.of(intervalReq(0, 1200), intervalReq(1300, 1500)));
//        testClient.setIntervalsForUser(meetingId, user3, List.of(intervalReq(200, 600), intervalReq(700, 1100)));
//
//        verifyUserWordsResponse(
//                new MeetingResponse(meetingId,
//                                    Map.of(user1, List.of(intervalRes(100, 300), intervalRes(500, 1000)),
//                                           user2, List.of(intervalRes(0, 1200), intervalRes(1300, 1500)),
//                                           user3, List.of(intervalRes(200, 600), intervalRes(700, 1100))),
//                                    List.of(intervalRes(200, 300), intervalRes(500, 600), intervalRes(700, 1000))));
//    }

    private void verifyUserWordsResponse(UserWordsResponse expectedValue) {
        testClient.getUserWords(expectedValue.userId())
                  .expectStatus()
                  .isOk()
                  .expectBody(UserWordsResponse.class)
                  .isEqualTo(expectedValue);
    }

//    private String createUserWords() {
//        return Objects.requireNonNull(testClient.createMeetingAndReturnEntity().getResponseBody()).id();
//    }

}
