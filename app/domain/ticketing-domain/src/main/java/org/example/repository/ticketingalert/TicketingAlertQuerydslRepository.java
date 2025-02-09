package org.example.repository.ticketingalert;

import java.time.LocalDateTime;
import java.util.List;
import org.example.entity.TicketingAlert;

public interface TicketingAlertQuerydslRepository {

    List<TicketingAlert> findAllByFcmTokenAndAlertTimeAfterNow(String fcmToken, LocalDateTime now);
}
