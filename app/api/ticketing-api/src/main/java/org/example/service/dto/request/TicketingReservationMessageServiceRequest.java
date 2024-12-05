package org.example.service.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.example.dto.request.TicketingReservationMessageDomainRequest;

@Builder
public record TicketingReservationMessageServiceRequest(
    String userFcmToken,
    String name,
    UUID showId,
    LocalDateTime ticketingAt,
    List<LocalDateTime> addAlertAts,
    List<LocalDateTime> deleteAlertAts
) {

    public TicketingReservationMessageDomainRequest toDomainRequest() {
        return TicketingReservationMessageDomainRequest.builder()
            .userFcmToken(userFcmToken)
            .name(name)
            .showId(showId)
            .ticketingAt(ticketingAt)
            .addAlertAts(addAlertAts)
            .deleteAlertAts(deleteAlertAts)
            .build();
    }
}
