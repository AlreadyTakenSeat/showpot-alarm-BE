package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticketing_alert")
public class TicketingAlert extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "schedule_alert_time", nullable = false)
    private LocalDateTime alertTime;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "show_id", nullable = false)
    private UUID showId;

    @Column(name = "ticketing_time", nullable = false)
    private LocalDateTime ticketingTime;

    @Builder
    private TicketingAlert(
            String name,
            LocalDateTime alertTime,
            UUID userId,
            UUID showId,
            LocalDateTime ticketingTime) {
        this.name = name;
        this.alertTime = alertTime;
        this.userId = userId;
        this.showId = showId;
        this.ticketingTime = ticketingTime;
    }
}
