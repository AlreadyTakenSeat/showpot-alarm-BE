package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.batch.TicketingAlertBatch;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;
import org.example.service.dto.request.TicketingReservationMessageServiceRequest;
import org.example.service.dto.response.TicketingAlertServiceRequest;
import org.example.usecase.TicketingAlertUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketingAlertService {

    private final TicketingAlertUseCase ticketingAlertUseCase;
    private final TicketingAlertBatch ticketingAlertBatchComponent;

    public void reserveTicketingAlert(TicketingReservationMessageServiceRequest request) {
        TicketingAlertToSchedulerDomainResponse ticketingAlertToScheduler = ticketingAlertUseCase
            .reserveTicketingAlert(request.toDomainRequest());

        ticketingAlertBatchComponent.reserveTicketingAlerts(
            TicketingAlertServiceRequest.from(ticketingAlertToScheduler)
        );
    }
}
