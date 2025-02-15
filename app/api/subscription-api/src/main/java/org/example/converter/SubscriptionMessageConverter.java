package org.example.converter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.conver.MessageConverter;
import org.example.listener.dto.ArtistSubscriptionMessageApiRequest;
import org.example.listener.dto.GenreSubscriptionMessageApiRequest;
import org.example.listener.dto.ShowRelationSubscriptionMessageApiRequest;
import org.springframework.data.redis.connection.Message;

@UtilityClass
@Slf4j
public final class SubscriptionMessageConverter {

    public ShowRelationSubscriptionMessageApiRequest toShowRelationSubscriptionMessage(
        Message message) {
        return MessageConverter.convertMessage(message,
            ShowRelationSubscriptionMessageApiRequest.class);
    }

    public ArtistSubscriptionMessageApiRequest toArtistSubscriptionMessage(Message message) {
        return MessageConverter.convertMessage(message, ArtistSubscriptionMessageApiRequest.class);
    }

    public GenreSubscriptionMessageApiRequest toGenreSubscriptionMessage(Message message) {
        return MessageConverter.convertMessage(message, GenreSubscriptionMessageApiRequest.class);
    }
}
