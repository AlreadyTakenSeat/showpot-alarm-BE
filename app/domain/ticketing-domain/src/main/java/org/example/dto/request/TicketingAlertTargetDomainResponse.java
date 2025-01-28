package org.example.dto.request;

import lombok.Builder;

import org.example.entity.TicketingAlert;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TicketingAlertTargetDomainResponse(
        UUID userId, UUID showId, String name, LocalDateTime ticketingAt) {
    public static TicketingAlertTargetDomainResponse from(TicketingAlert ticketingAlert) {
        return TicketingAlertTargetDomainResponse.builder()
            .userId(ticketingAlert.getUserId())
            .showId(ticketingAlert.getShowId())
            .name(ticketingAlert.getName())
            .ticketingAt(ticketingAlert.getTicketingTime())
            .build();
    }
}
