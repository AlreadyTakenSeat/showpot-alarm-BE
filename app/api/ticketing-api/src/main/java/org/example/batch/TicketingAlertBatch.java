package org.example.batch;

import java.util.List;
import org.example.entity.TicketingAlert;
import org.example.service.dto.response.TicketingAlertServiceRequest;

public interface TicketingAlertBatch {

    void reserveTicketingAlerts(TicketingAlertServiceRequest ticketingAlert);

    void updateTicketingAlerts(String userFcmToken, List<TicketingAlert> ticketingAlerts);
}
