package org.example.conver;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

@UtilityClass
@Slf4j
public class MessageConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertMessage(Message message, Class<T> targetType) {
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
