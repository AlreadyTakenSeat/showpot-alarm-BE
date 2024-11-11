package org.example.controller.dto.response;

import org.example.service.dto.response.ShowAlarmActivateServiceResponse;

public record ShowAlarmActivateApiResponse(
    boolean isActivate
) {

    public static ShowAlarmActivateApiResponse from(ShowAlarmActivateServiceResponse response) {
        return new ShowAlarmActivateApiResponse(response.isActivate());
    }

}
