package org.example.listener.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.example.service.dto.request.TicketingReservationMessageServiceRequest;

public record TicketingReservationMessageApiRequest(
    String userFcmToken,
    String name,
    UUID showId,
    String ticketingAt,
    List<String> addAlertAts,
    List<String> deleteAlertAts
) {

    public TicketingReservationMessageServiceRequest toServiceRequest() {
        return TicketingReservationMessageServiceRequest.builder()
            .userFcmToken(userFcmToken)
            .name(name)
            .showId(showId)
            .ticketingAt(LocalDateTime.parse(ticketingAt))
            .addAlertAts(addAlertAts.stream().map(LocalDateTime::parse).toList())
            .addAlertAts(deleteAlertAts.stream().map(LocalDateTime::parse).toList())
            .build();
    }
}
