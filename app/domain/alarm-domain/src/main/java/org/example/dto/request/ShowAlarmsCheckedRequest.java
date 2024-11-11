package org.example.dto.request;

import java.util.UUID;
import lombok.Builder;
import org.example.dto.response.ShowAlarmDomainResponse;

@Builder
public record ShowAlarmsCheckedRequest(
    UUID id,
    boolean checked
) {
    public static ShowAlarmsCheckedRequest from(ShowAlarmDomainResponse response) {
        return ShowAlarmsCheckedRequest.builder()
            .id(response.id())
            .checked(response.checked())
            .build();
    }

}
