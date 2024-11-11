package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ShowAlarmsCheckedRequest;
import org.example.dto.response.PaginationServiceResponse;
import org.example.service.dto.param.ShowAlarmPaginationServiceParam;
import org.example.service.dto.request.ShowAlarmsServiceRequest;
import org.example.service.dto.response.ShowAlarmActivateServiceResponse;
import org.example.usecase.ShowAlarmUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowAlarmService {

    private final ShowAlarmUseCase showAlarmUseCase;


    public PaginationServiceResponse<ShowAlarmPaginationServiceParam> getShowAlarms(
        ShowAlarmsServiceRequest request
    ) {
        var response = showAlarmUseCase.findShowAlarms(request.toDomainRequest());
        showAlarmUseCase.updateUncheckedAlarms(response.data().stream()
            .map(ShowAlarmsCheckedRequest::from)
            .toList()
        );

        List<ShowAlarmPaginationServiceParam> data = response.data().stream()
            .map(ShowAlarmPaginationServiceParam::from)
            .toList();

        return PaginationServiceResponse.of(data, response.hasNext());
    }

    public ShowAlarmActivateServiceResponse getShowAlarmActivate(String fcmToken) {
        return ShowAlarmActivateServiceResponse.from(showAlarmUseCase.hasUncheckedAlarms(fcmToken));
    }
}
