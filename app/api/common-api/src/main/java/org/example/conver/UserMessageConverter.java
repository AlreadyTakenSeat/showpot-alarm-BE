package org.example.conver;

import lombok.experimental.UtilityClass;
import org.example.conver.message.UserFcmTokenMessage;
import org.springframework.data.redis.connection.Message;

@UtilityClass
public class UserMessageConverter {

    public UserFcmTokenMessage toUserFcmMessage(Message message) {
        return MessageConverter.convertMessage(message, UserFcmTokenMessage.class);
    }

}
