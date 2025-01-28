package org.example.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.example.dto.request.TicketingAlertTargetDomainResponse;
import org.example.dto.request.TicketingReservationMessageDomainRequest;

@Builder
public record TicketingAlertToSchedulerDomainResponse(
        String name,
        UUID userId,
        UUID showId,
        LocalDateTime ticketingAt,
        List<LocalDateTime> addAlertAts,
        List<LocalDateTime> deleteAlertAts) {

    public static TicketingAlertToSchedulerDomainResponse as(
            TicketingReservationMessageDomainRequest request,
            List<LocalDateTime> addAlertAts,
            List<LocalDateTime> deleteAlertAts) {
        return TicketingAlertToSchedulerDomainResponse.builder()
            .name(request.name())
            .userId(request.userId())
            .showId(request.showId())
            .ticketingAt(request.ticketingAt())
            .addAlertAts(addAlertAts)
            .deleteAlertAts(deleteAlertAts)
            .build();
    }

    public static TicketingAlertToSchedulerDomainResponse as(
            TicketingAlertTargetDomainResponse key, List<LocalDateTime> value) {
        return TicketingAlertToSchedulerDomainResponse.builder()
            .name(key.name())
            .userId(key.userId())
            .showId(key.showId())
            .ticketingAt(key.ticketingAt())
            .addAlertAts(value)
            .deleteAlertAts(List.of())
            .build();
    }
}
