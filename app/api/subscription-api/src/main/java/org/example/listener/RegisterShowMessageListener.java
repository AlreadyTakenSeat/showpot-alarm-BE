package org.example.listener;

import lombok.RequiredArgsConstructor;
import org.example.converter.SubscriptionMessageConverter;
import org.example.metric.MessageQueueSubMonitored;
import org.example.service.SubscriptionAlarmService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Qualifier(value = "registerShowMessageListener")
public class RegisterShowMessageListener implements MessageListener {

    private final SubscriptionAlarmService subscriptionAlarmService;

    @Override
    @MessageQueueSubMonitored(topic = "show")
    public void onMessage(Message message, byte[] pattern) {
        var request = SubscriptionMessageConverter.toShowRelationSubscriptionMessage(message);
        subscriptionAlarmService.showRelationSubscription(request.toServiceRequest());
    }

}
