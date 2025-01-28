package org.example.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TicketingReservationMessageDomainRequest(
    UUID userId,
    String name,
    UUID showId,
    LocalDateTime ticketingAt,
    List<LocalDateTime> addAlertAts,
    List<LocalDateTime> deleteAlertAts
) {
}
