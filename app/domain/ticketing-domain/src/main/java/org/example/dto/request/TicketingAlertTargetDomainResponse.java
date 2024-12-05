package org.example.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.example.entity.TicketingAlert;

@Builder
public record TicketingAlertTargetDomainResponse(
    String userFcmToken,
    UUID showId,
    String name,
    LocalDateTime ticketingAt
) {
    public static TicketingAlertTargetDomainResponse from(TicketingAlert ticketingAlert) {
        return TicketingAlertTargetDomainResponse.builder()
            .userFcmToken(ticketingAlert.getUserFcmToken())
            .showId(ticketingAlert.getShowId())
            .name(ticketingAlert.getName())
            .ticketingAt(ticketingAlert.getTicketingTime())
            .build();
    }

}
