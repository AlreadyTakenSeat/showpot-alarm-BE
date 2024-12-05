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
    String userFcmToken,
    UUID showId,
    LocalDateTime ticketingAt,
    List<LocalDateTime> addAlertAts,
    List<LocalDateTime> deleteAlertAts
) {

    public static TicketingAlertToSchedulerDomainResponse as(
        TicketingReservationMessageDomainRequest request,
        List<LocalDateTime> addAlertAts,
        List<LocalDateTime> deleteAlertAts
    ) {
        return TicketingAlertToSchedulerDomainResponse.builder()
            .name(request.name())
            .userFcmToken(request.userFcmToken())
            .showId(request.showId())
            .ticketingAt(request.ticketingAt())
            .addAlertAts(addAlertAts)
            .deleteAlertAts(deleteAlertAts)
            .build();
    }


    public static TicketingAlertToSchedulerDomainResponse as(
        TicketingAlertTargetDomainResponse key,
        List<LocalDateTime> value
    ) {
        return TicketingAlertToSchedulerDomainResponse.builder()
            .name(key.name())
            .userFcmToken(key.userFcmToken())
            .showId(key.showId())
            .ticketingAt(key.ticketingAt())
            .addAlertAts(value)
            .deleteAlertAts(List.of())
            .build();
    }
}
