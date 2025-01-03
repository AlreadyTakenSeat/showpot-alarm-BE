package org.example.repository.ticketingalert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.example.entity.TicketingAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketingAlertRepository extends JpaRepository<TicketingAlert, UUID> {

    List<TicketingAlert> findAllByUserFcmTokenAndShowIdAndIsDeletedFalse(
        String userFcmToken,
        UUID showId
    );

    List<TicketingAlert> findAllByIsDeletedFalseAndAlertTimeAfter(LocalDateTime alertTime);
}
