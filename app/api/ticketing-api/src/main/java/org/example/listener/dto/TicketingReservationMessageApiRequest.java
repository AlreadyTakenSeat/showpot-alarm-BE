package org.example.listener.dto;

import org.example.service.dto.request.TicketingReservationMessageServiceRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicketingReservationMessageApiRequest(
        UUID userId,
        String name,
        UUID showId,
        String ticketingAt,
        List<String> addAlertAts,
        List<String> deleteAlertAts) {

    public TicketingReservationMessageServiceRequest toServiceRequest() {
        return TicketingReservationMessageServiceRequest.builder()
            .userId(userId)
            .name(name)
            .showId(showId)
            .ticketingAt(LocalDateTime.parse(ticketingAt))
            .addAlertAts(addAlertAts.stream().map(LocalDateTime::parse).toList())
            .deleteAlertAts(deleteAlertAts.stream().map(LocalDateTime::parse).toList())
            .build();
    }
}
