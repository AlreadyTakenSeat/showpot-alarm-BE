package org.example.batch;

import org.example.service.dto.response.TicketingAlertServiceRequest;

public interface TicketingAlertBatch {

    void reserveTicketingAlerts(TicketingAlertServiceRequest ticketingAlert);

}
