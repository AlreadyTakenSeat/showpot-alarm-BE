package org.example.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.listener.dto.ArtistSubscriptionMessageApiRequest;
import org.example.listener.dto.GenreSubscriptionMessageApiRequest;
import org.example.listener.dto.ShowRelationSubscriptionMessageApiRequest;
import org.springframework.data.redis.connection.Message;

@Slf4j
public final class SubscriptionMessageConverter {

    private SubscriptionMessageConverter() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ShowRelationSubscriptionMessageApiRequest toShowRelationSubscriptionMessage(Message message) {
        return convertMessage(message, ShowRelationSubscriptionMessageApiRequest.class);
    }

    public static ArtistSubscriptionMessageApiRequest toArtistSubscriptionMessage(Message message) {
        return convertMessage(message, ArtistSubscriptionMessageApiRequest.class);
    }

    public static GenreSubscriptionMessageApiRequest toGenreSubscriptionMessage(Message message) {
        return convertMessage(message, GenreSubscriptionMessageApiRequest.class);
    }

    private static <T> T convertMessage(Message message, Class<T> targetType) {
        try {
            T convertedMessage = objectMapper.readValue(message.getBody(), targetType);
            log.info("Message published successfully to topic: {}", new String(message.getChannel()));
            log.info("Subscribe Message Contents ( {} : {} )", targetType.getName(), message);
            return convertedMessage;
        } catch (IOException e) {
            log.error("Failed to convert message to {}", targetType.getName(), e);
            throw new IllegalArgumentException("메시지를 받지 못했습니다.");
        }
    }
}
