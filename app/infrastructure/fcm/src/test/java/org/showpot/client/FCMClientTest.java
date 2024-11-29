package org.showpot.client;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.List;
import java.util.stream.Stream;
import org.example.message.MessageParam;
import org.example.message.PushMessageTemplate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.showpot.dto.param.FCMMessageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled
@SpringBootTest
@ActiveProfiles("fcm-local")
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class FCMClientTest {

    @Autowired
    private FCMClient fcmClient;

    @Disabled
    @ParameterizedTest
    @MethodSource("getMessage")
    public void sendSingleUser(MessageParam message) {
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);

        fcmClient.sendNotification(
            "drWA5h0SSSexoBCKAyS-V1:"
                + "APA91bEKrDOCw8kQqz8Jy4I1CouJrgFklZyt5iTJNHJsZYB87IdbCoC-"
                + "FwPS7SD3My2f3fDuzFmHLwAUjr6__bdIQyn627yPuoG1yorECD0yBhXsK1zdcJInNFAk6jBaOjOHx09xKRZk",
            fcmMessage.toNotification()
        );
    }

    @Disabled
    @Test
    public void sendSingleUser() throws FirebaseMessagingException {
        MessageParam message = PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24");
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);

        fcmClient.sendNotification(
            "cB9rtWzKVkBWglacCkCvF4:"
                + "APA91bGzqxp-giGM3wyh7yuXmaQMuqg-DNaavA-"
                + "l5UiQxP3rmxzWRosRw6ZQFrm1ECccBaDTUnQcTf"
                + "0YaBLsEwTgw5NKB6NwEnaaweFJ1bc7Ueg14iMfmP3k7o3DpEBw7WiIqupQzFYd",
            fcmMessage.toNotification()
        );
    }

    @Disabled
    @Test
    public void sendFailureSingleUser() throws FirebaseMessagingException {
        MessageParam message = PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24");
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);

        fcmClient.sendNotification(
            "cB9rtWzKVkBWglacCkCvF4:abc",
            fcmMessage.toNotification()
        );
    }

    @Disabled
    @Test
    public void sendMultiUser() throws FirebaseMessagingException {
        MessageParam message = PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24");
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);
        List<String> fcmTokens = List.of(
            "cB9rtWzKVkBWglacCkCvF4:"
                + "APA91bGzqxp-giGM3wyh7yuXmaQMuqg-DNaavA-"
                + "l5UiQxP3rmxzWRosRw6ZQFrm1ECccBaDTUnQcTf"
                + "0YaBLsEwTgw5NKB6NwEnaaweFJ1bc7Ueg14iMfmP3k7o3DpEBw7WiIqupQzFYd",
            "drWA5h0SSSexoBCKAyS-V1:"
                + "APA91bEKrDOCw8kQqz8Jy4I1CouJrgFklZyt5iTJNHJsZYB87IdbCoC-"
                + "FwPS7SD3My2f3fDuzFmHLwAUjr6__bdIQyn627yPuoG1yorECD0yBhXsK1zdcJInNFAk6jBaOjOHx09xKRZk"
        );

        fcmClient.sendNotification(fcmTokens, fcmMessage.toNotification());
    }

    @Disabled
    @Test
    public void allFail() {
        MessageParam message = PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24");
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);
        List<String> fcmTokens = List.of(
            "cB9rtWzKVkBWglacCkCvF4:abc",
            "drWA5h0SSSexoBCKAyS-V1:def"
        );

        fcmClient.sendNotification(fcmTokens, fcmMessage.toNotification());
    }

    @Disabled
    @Test
    public void failAndSuccess() {
        MessageParam message = PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24");
        FCMMessageParam fcmMessage = FCMMessageParam.from(message);
        List<String> fcmTokens = List.of(
            "cB9rtWzKVkBWglacCkCvF4:abc",
            "drWA5h0SSSexoBCKAyS-V1:"
                + "APA91bEKrDOCw8kQqz8Jy4I1CouJrgFklZyt5iTJNHJsZYB87IdbCoC-"
                + "FwPS7SD3My2f3fDuzFmHLwAUjr6__bdIQyn627yPuoG1yorECD0yBhXsK1zdcJInNFAk6jBaOjOHx09xKRZk"
        );

        fcmClient.sendNotification(fcmTokens, fcmMessage.toNotification());
    }

    private static Stream<Arguments> getMessage() {
        return Stream.of(
            Arguments.of(PushMessageTemplate.getTicketingAlertMessageBefore30Minutes("공연제목", "24")),
            Arguments.of(PushMessageTemplate.getTicketingAlertMessageBefore10Minutes("공연제목", "6")),
            Arguments.of(PushMessageTemplate.getTicketingAlertMessageBefore5Minutes("공연제목", "1")),
            Arguments.of(PushMessageTemplate.getSubscribedArtistVisitKoreaAlertMessage("아티스트이름")),
            Arguments.of(PushMessageTemplate.getSubscribedGenreVisitKoreaAlertMessage("장르이름"))
        );
    }
}
