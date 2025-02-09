package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.batch.TicketingAlertBatch;
import org.example.conver.message.UserFcmTokenMessage;
import org.example.entity.TicketingAlert;
import org.example.usecase.TicketingAlertUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAlertServiceImpl implements UserAlertService {

    private final TicketingAlertUseCase ticketingAlertUseCase;
    private final TicketingAlertBatch ticketingAlertBatchComponent;

    @Override
    public void updateUserFcmToken(UserFcmTokenMessage request) {
        List<TicketingAlert> ticketingAlerts = ticketingAlertUseCase.updateUserFcmToken(
            request.previousFcmToken(), request.updatedFcmToken());

        ticketingAlertBatchComponent.updateTicketingAlerts(
            request.previousFcmToken(), ticketingAlerts);
    }
}
