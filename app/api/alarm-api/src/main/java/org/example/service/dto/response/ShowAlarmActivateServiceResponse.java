package org.example.service.dto.response;

public record ShowAlarmActivateServiceResponse(
    boolean isActivate
) {

    public static ShowAlarmActivateServiceResponse from(boolean hasUncheckedAlarms) {
        return new ShowAlarmActivateServiceResponse(hasUncheckedAlarms);
    }

}
