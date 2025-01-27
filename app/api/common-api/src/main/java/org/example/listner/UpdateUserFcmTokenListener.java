package org.example.listner;

import lombok.RequiredArgsConstructor;
import org.example.conver.UserMessageConverter;
import org.example.metric.MessageQueueSubMonitored;
import org.example.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Qualifier(value = "updateUserFcmTokenListener")
public class UpdateUserFcmTokenListener implements MessageListener {

    private final UserSubscriptionService userSubscriptionServiceImpl;

    @Override
    @MessageQueueSubMonitored(topic = "updateUserFcmToken")
    public void onMessage(Message message, byte[] pattern) {
        var request = UserMessageConverter.toUserFcmMessage(message);

        userSubscriptionServiceImpl.updateUserFcmToken(request);
    }
}
