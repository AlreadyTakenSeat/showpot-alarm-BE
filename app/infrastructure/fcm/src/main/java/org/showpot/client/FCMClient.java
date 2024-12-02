package org.showpot.client;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metric.OpenApiMonitored;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FCMClient {

    private final FirebaseMessaging firebaseMessaging;

    @OpenApiMonitored(name = "fcmSingle")
    public void sendNotification(String fcmToken, Notification notification) {
        try {
            firebaseMessaging.send(
                Message.builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error(
                """
                    푸시 알림 전송에 실패했습니다. 
                    실패 사유: {} 
                    fcm_token: {}""",
                e.getMessagingErrorCode().name(),
                fcmToken
            );
        }
    }

    @OpenApiMonitored(name = "fcmMulti")
    public void sendNotification(List<String> fcmTokens, Notification notification) {
        if (fcmTokens.size() > 500) {
            log.warn("한 번에 요청할 수 있는 푸시 알림의 수는 500개입니다.");
            throw new IllegalArgumentException("한 번에 요청할 수 있는 푸시 알림의 수는 500개입니다.");
        }

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(
                MulticastMessage.builder()
                    .addAllTokens(fcmTokens)
                    .setNotification(notification)
                    .build()
            );

            loggingFirebaseMessaging(response);
        } catch (FirebaseMessagingException e) {
            log.error("푸시 알림 전송에 실패했습니다.", e);
        }
    }

    private void loggingFirebaseMessaging(BatchResponse response) {
        log.info(
            """
                푸시 알림 전송 결과: 성공 {}, 실패 {}
                에러코드별 실패 횟수
                {}
                """,
            response.getSuccessCount(),
            response.getFailureCount(),
            getCountByErrorCode(response)
        );
    }

    private Map<String, Integer> getCountByErrorCode(BatchResponse response) {
        return response.getResponses().stream()
            .filter(it -> !it.isSuccessful())
            .collect(
                Collectors.groupingBy(
                    it -> it.getException().getMessagingErrorCode().name(),
                    Collectors.summingInt(it -> 1)
                )
            );
    }
}
