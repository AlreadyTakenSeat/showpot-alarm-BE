package org.example.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShowAlarmDomainResponse(
    UUID id,
    UUID showId,
    String title,
    String content,
    LocalDateTime createAt,
    boolean checked
) {

}
