package org.example.controller;

import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.controller.dto.param.ShowAlarmPaginationApiParam;
import org.example.controller.dto.response.ShowAlarmActivateApiResponse;
import org.example.dto.response.CursorApiResponse;
import org.example.dto.response.PaginationApiResponse;
import org.example.service.ShowAlarmService;
import org.example.service.dto.request.ShowAlarmsServiceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/show-alarm")
public class ShowAlarmController {

    private final ShowAlarmService showAlarmService;

    @PostMapping
    public ResponseEntity<PaginationApiResponse<ShowAlarmPaginationApiParam>> getShowAlarms(
        @RequestParam(value = "fcmToken") String fcmToken,
        @RequestParam(value = "cursorId", required = false) UUID cursorId,
        @RequestParam(value = "cursorValue", required = false) LocalDateTime cursorValue,
        @RequestParam(value = "size") @Max(value = 30, message = "조회하는 데이터의 최대 개수는 30입니다.")
        Integer size
    ) {
        var response = showAlarmService.getShowAlarms(
            ShowAlarmsServiceRequest.builder()
                .fcmToken(fcmToken)
                .cursorId(cursorId)
                .cursorValue(cursorValue)
                .size(size)
                .build()
        );
        var data = response.data().stream()
            .map(ShowAlarmPaginationApiParam::from)
            .toList();

        CursorApiResponse cursor = Optional.ofNullable(CursorApiResponse.getLastElement(data))
            .map(element -> CursorApiResponse.toCursorResponse(element.id(), element.createAt()))
            .orElse(CursorApiResponse.noneCursor());

        return ResponseEntity.ok(
            PaginationApiResponse.<ShowAlarmPaginationApiParam>builder()
                .hasNext(response.hasNext())
                .data(data)
                .cursor(cursor)
                .build()
        );
    }

    @GetMapping("/checked")
    public ResponseEntity<ShowAlarmActivateApiResponse> getShowAlarmActivate(
        @RequestParam(value = "fcmToken") String fcmToken
    ) {
        return ResponseEntity.ok(
            ShowAlarmActivateApiResponse.from(
                showAlarmService.getShowAlarmActivate(fcmToken)
            )
        );
    }
}
