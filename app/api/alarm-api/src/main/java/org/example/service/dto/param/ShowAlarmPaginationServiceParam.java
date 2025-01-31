package org.example.service.dto.param;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.example.dto.response.ShowAlarmDomainResponse;

@Builder
public record ShowAlarmPaginationServiceParam(
    UUID id,
    UUID showId,
    String title,
    String content,
    LocalDateTime createAt
) {

    public static ShowAlarmPaginationServiceParam from(ShowAlarmDomainResponse response) {
        return ShowAlarmPaginationServiceParam.builder()
            .id(response.id())
            .showId(response.showId())
            .title(response.title())
            .content(response.content())
            .createAt(response.createAt())
            .build();
    }
}
