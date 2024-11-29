package org.example.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;

@Builder
public record TicketingAlertServiceRequest(
    String name,
    String userFcmToken,
    UUID showId,
    LocalDateTime ticketingAt,
    List<LocalDateTime> addAlertAts,
    List<LocalDateTime> deleteAlertAts
) {

    public static TicketingAlertServiceRequest from(
        TicketingAlertToSchedulerDomainResponse ticketingAlert
    ) {
        return TicketingAlertServiceRequest.builder()
            .name(ticketingAlert.name())
            .userFcmToken(ticketingAlert.userFcmToken())
            .showId(ticketingAlert.showId())
            .ticketingAt(ticketingAlert.ticketingAt())
            .addAlertAts(ticketingAlert.addAlertAts())
            .deleteAlertAts(ticketingAlert.deleteAlertAts())
            .build();
    }
}
