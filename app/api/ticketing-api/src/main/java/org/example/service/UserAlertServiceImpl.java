package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.conver.message.UserFcmTokenMessage;
import org.example.usecase.TicketingAlertUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAlertServiceImpl implements UserAlertService {

    private final TicketingAlertUseCase ticketingAlertUseCase;

    @Override
    public void updateUserFcmToken(UserFcmTokenMessage request) {
        ticketingAlertUseCase.updateUserFcmToken(request.previousFcmToken(), request.updatedFcmToken());
    }
}
