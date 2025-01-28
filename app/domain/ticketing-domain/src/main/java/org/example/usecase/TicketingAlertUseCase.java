package org.example.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.TicketingAlertTargetDomainResponse;
import org.example.dto.request.TicketingReservationMessageDomainRequest;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;
import org.example.entity.BaseEntity;
import org.example.entity.TicketingAlert;
import org.example.repository.ticketingalert.TicketingAlertRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TicketingAlertUseCase {

    private final TicketingAlertRepository ticketingAlertRepository;

    public List<TicketingAlertToSchedulerDomainResponse> findAllTicketingAlerts() {
        Map<TicketingAlertTargetDomainResponse, List<LocalDateTime>> groupedAlerts =
            ticketingAlertRepository
                .findAllByIsDeletedFalseAndAlertTimeAfter(LocalDateTime.now())
                .stream()
                .collect(
                    Collectors.groupingBy(
                        TicketingAlertTargetDomainResponse::from,
                        Collectors.mapping(
                            TicketingAlert::getAlertTime,
                                    Collectors.toList()
                        )
                    ));

        return groupedAlerts.entrySet().stream()
            .map(entry -> TicketingAlertToSchedulerDomainResponse.as(
                    entry.getKey(),
                    entry.getValue()
                )
            )
            .toList();
    }

    @Transactional
    public TicketingAlertToSchedulerDomainResponse reserveTicketingAlert(
        TicketingReservationMessageDomainRequest ticketingReservations
    ) {
        return TicketingAlertToSchedulerDomainResponse.as(
            ticketingReservations,
            addAlerts(ticketingReservations),
            removeAlerts(ticketingReservations));
    }

    private List<LocalDateTime> addAlerts(
            TicketingReservationMessageDomainRequest ticketingReservations) {
        List<TicketingAlert> alertsToAdd = ticketingReservations.addAlertAts().stream()
            .map(addAt -> TicketingAlert.builder()
                .name(ticketingReservations.name())
                .alertTime(addAt)
                .userId(ticketingReservations.userId())
                .showId(ticketingReservations.showId())
                .ticketingTime(ticketingReservations.ticketingAt())
                .build()
            )
            .toList();
        ticketingAlertRepository.saveAll(alertsToAdd);

        return alertsToAdd.stream().map(TicketingAlert::getAlertTime).toList();
    }

    private List<LocalDateTime> removeAlerts(
            TicketingReservationMessageDomainRequest ticketingReservations) {
        List<TicketingAlert> existingAlerts =
            ticketingAlertRepository.findAllByUserIdAndShowIdAndIsDeletedFalse(
                ticketingReservations.userId(), ticketingReservations.showId());

        List<TicketingAlert> alertsToRemove =
            existingAlerts.stream()
                .filter(alert -> ticketingReservations.deleteAlertAts().contains(alert.getAlertTime()))
                .toList();
        alertsToRemove.forEach(BaseEntity::softDelete);

        return alertsToRemove.stream().map(TicketingAlert::getAlertTime).toList();
    }
}
