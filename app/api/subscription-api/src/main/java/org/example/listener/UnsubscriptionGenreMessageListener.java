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
@Qualifier(value = "unsubscriptionGenreMessageListener")
public class UnsubscriptionGenreMessageListener implements MessageListener {

    private final SubscriptionAlarmService subscriptionAlarmService;

    @Override
    @MessageQueueSubMonitored(topic = "genreUnsubscription")
    public void onMessage(Message message, byte[] pattern) {
        var request = SubscriptionMessageConverter.toGenreSubscriptionMessage(message);
        subscriptionAlarmService.genreUnsubscribe(request.toServiceRequest());
    }

}
