package org.showpot.dto.param;

import com.google.firebase.messaging.Notification;
import lombok.Builder;
import org.example.message.MessageParam;

@Builder
public record FCMMessageParam(
    String title,
    String body
) {

    public static FCMMessageParam from(MessageParam param) {
        return FCMMessageParam.builder()
            .title(param.getTitle())
            .body(param.getBody())
            .build();
    }

    public Notification toNotification() {
        return Notification.builder()
            .setTitle(title())
            .setBody(body())
            .build();
    }
}
