package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticketing_alert")
public class TicketingAlert extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "schedule_alert_time", nullable = false)
    private LocalDateTime alertTime;

    @Column(name = "user_fcm_token", nullable = false)
    private String userFcmToken;

    @Column(name = "show_id", nullable = false)
    private UUID showId;

    @Column(name = "ticketing_time", nullable = false)
    private LocalDateTime ticketingTime;

    @Builder
    private TicketingAlert(
        String name,
        LocalDateTime alertTime,
        String userFcmToken,
        UUID showId,
        LocalDateTime ticketingTime
    ) {
        this.name = name;
        this.alertTime = alertTime;
        this.userFcmToken = userFcmToken;
        this.showId = showId;
        this.ticketingTime = ticketingTime;
    }
}
