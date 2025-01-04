package org.example.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ShowAlarmsDomainRequest(
    String fcmToken,
    UUID cursorId,
    LocalDateTime cursorValue,
    Integer size
) {

}
