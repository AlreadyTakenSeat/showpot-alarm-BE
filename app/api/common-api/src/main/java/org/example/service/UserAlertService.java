package org.example.service;

import org.example.conver.message.UserFcmTokenMessage;

public interface UserAlertService {

    void updateUserFcmToken(UserFcmTokenMessage request);
}
