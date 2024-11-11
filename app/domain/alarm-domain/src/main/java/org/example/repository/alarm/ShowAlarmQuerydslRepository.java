package org.example.repository.alarm;

import org.example.dto.request.ShowAlarmsDomainRequest;
import org.example.dto.response.ShowAlarmPaginationDomainResponse;

public interface ShowAlarmQuerydslRepository {

    ShowAlarmPaginationDomainResponse findAllWithCursorPagination(ShowAlarmsDomainRequest request);

}
