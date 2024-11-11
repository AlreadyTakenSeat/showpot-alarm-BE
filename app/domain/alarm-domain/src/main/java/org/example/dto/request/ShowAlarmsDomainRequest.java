package org.example.dto.request;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ShowAlarmsDomainRequest(
    String fcmToken,
    UUID cursorId,
    Integer size
) {

}
