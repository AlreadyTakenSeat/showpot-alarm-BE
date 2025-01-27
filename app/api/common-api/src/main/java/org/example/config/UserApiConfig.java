package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.ErrorHandler;

@Configuration
@Import(PubSubConfig.class)
@RequiredArgsConstructor
public class UserApiConfig {

    private final MessageListener updateUserFcmTokenListener;
    private final ErrorHandler redisSubErrorHandler;


    @Bean
    MessageListenerAdapter updateUserFcmTokenListenerAdapter() {
        return new MessageListenerAdapter(updateUserFcmTokenListener);
    }

    @Bean
    RedisMessageListenerContainer updateUserFcmTokenListenerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter updateUserFcmTokenListenerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(updateUserFcmTokenListenerAdapter,
            ChannelTopic.of("userFcmToken"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }
}
