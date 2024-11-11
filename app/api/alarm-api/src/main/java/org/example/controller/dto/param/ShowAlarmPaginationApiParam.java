package org.example.controller.dto.param;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.example.service.dto.param.ShowAlarmPaginationServiceParam;

@Builder
public record ShowAlarmPaginationApiParam(
    UUID id,
    UUID showId,
    String title,
    String content,
    LocalDateTime createAt
) {

    public static ShowAlarmPaginationApiParam from(ShowAlarmPaginationServiceParam param) {
        return ShowAlarmPaginationApiParam.builder()
            .id(param.id())
            .showId(param.showId())
            .title(param.title())
            .content(param.content())
            .createAt(param.createAt())
            .build();
    }
}
