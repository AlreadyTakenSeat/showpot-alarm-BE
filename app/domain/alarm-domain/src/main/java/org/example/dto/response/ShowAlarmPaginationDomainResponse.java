package org.example.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ShowAlarmPaginationDomainResponse(
    List<ShowAlarmDomainResponse> data,
    boolean hasNext
) {

}
