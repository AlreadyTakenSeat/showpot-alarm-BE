package org.example.usecase;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ShowAlarmsCheckedRequest;
import org.example.dto.request.ShowAlarmsDomainRequest;
import org.example.dto.response.ShowAlarmPaginationDomainResponse;
import org.example.entity.ShowAlarm;
import org.example.repository.alarm.ShowAlarmRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShowAlarmUseCase {

    private final ShowAlarmRepository showAlarmRepository;

    public void save(ShowAlarm showAlarm) {
        showAlarmRepository.save(showAlarm);
    }

    public ShowAlarmPaginationDomainResponse findShowAlarms(
        ShowAlarmsDomainRequest request
    ) {
        return showAlarmRepository.findAllWithCursorPagination(request);
    }

    @Transactional
    public void updateUncheckedAlarms(List<ShowAlarmsCheckedRequest> alarms) {
        alarms.stream()
            .filter(alarm -> !alarm.checked())
            .forEach(alarm -> {
                ShowAlarm showAlarm = showAlarmRepository.findByIdAndIsDeletedFalse(alarm.id())
                    .orElseThrow(NoSuchElementException::new);

                showAlarm.checked();
            });
    }

    public boolean hasUncheckedAlarms(String fcmToken) {
        return showAlarmRepository.existsByUserFcmTokenAndCheckedFalse(fcmToken);
    }
}
