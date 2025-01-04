package org.example.service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.example.dto.request.ShowAlarmsDomainRequest;

@Builder
public record ShowAlarmsServiceRequest(
    String fcmToken,
    UUID cursorId,
    LocalDateTime cursorValue,
    Integer size
) {
    public ShowAlarmsDomainRequest toDomainRequest() {
        return ShowAlarmsDomainRequest.builder()
            .fcmToken(fcmToken)
            .cursorId(cursorId)
            .cursorValue(cursorValue)
            .size(size)
            .build();
    }

}
