package org.example.conver.message;

import java.util.UUID;

public record UserFcmTokenMessage(
    UUID userId,
    String previousFcmToken,
    String updatedFcmToken
) {

}
