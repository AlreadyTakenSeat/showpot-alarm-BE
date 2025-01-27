package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.ErrorHandler;

@Configuration
@Import({
    SubscriptionDomainConfig.class,
    PubSubConfig.class
})
@ComponentScan(basePackages = "org.example")
@RequiredArgsConstructor
public class SubscriptionApiConfig {

    private final MessageListener registerShowMessageListener;
    private final MessageListener updateShowMessageListener;
    private final MessageListener subscriptionArtistMessageListener;
    private final MessageListener unsubscriptionArtistMessageListener;
    private final MessageListener subscriptionGenreMessageListener;
    private final MessageListener unsubscriptionGenreMessageListener;
    private final ErrorHandler redisSubErrorHandler;


    @Bean
    public MessageListenerAdapter registerShowMessageListenerAdapter() {
        return new MessageListenerAdapter(registerShowMessageListener);
    }

    @Bean
    public MessageListenerAdapter updateShowMessageListenerAdapter() {
        return new MessageListenerAdapter(updateShowMessageListener);
    }

    @Bean
    public MessageListenerAdapter subscriptionArtistMessageListerAdapter() {
        return new MessageListenerAdapter(subscriptionArtistMessageListener);
    }

    @Bean
    public MessageListenerAdapter unsubscriptionArtistMessageListerAdapter() {
        return new MessageListenerAdapter(unsubscriptionArtistMessageListener);
    }

    @Bean
    public MessageListenerAdapter subscriptionGenreMessageListerAdapter() {
        return new MessageListenerAdapter(subscriptionGenreMessageListener);
    }

    @Bean
    public MessageListenerAdapter unsubscriptionGenreMessageListerAdapter() {
        return new MessageListenerAdapter(unsubscriptionGenreMessageListener);
    }

    @Bean
    public RedisMessageListenerContainer registerShowMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter registerShowMessageListenerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(registerShowMessageListenerAdapter,
            ChannelTopic.of("registerShow"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer updateShowMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter updateShowMessageListenerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(updateShowMessageListenerAdapter,
            ChannelTopic.of("updateShow"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer subscriptionArtistMessageListerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter subscriptionArtistMessageListerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriptionArtistMessageListerAdapter,
            ChannelTopic.of("artistSubscription"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer unsubscriptionArtistMessageListerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter unsubscriptionArtistMessageListerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(unsubscriptionArtistMessageListerAdapter,
            ChannelTopic.of("artistUnsubscription"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer subscriptionGenreMessageListerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter subscriptionGenreMessageListerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriptionGenreMessageListerAdapter,
            ChannelTopic.of("genreSubscription"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer unsubscriptionGenreMessageListerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter unsubscriptionGenreMessageListerAdapter
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(unsubscriptionGenreMessageListerAdapter,
            ChannelTopic.of("genreUnsubscription"));
        container.setErrorHandler(redisSubErrorHandler);
        return container;
    }

}
