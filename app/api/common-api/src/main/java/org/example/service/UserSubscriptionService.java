package org.example.service;

import org.example.conver.message.UserFcmTokenMessage;

public interface UserSubscriptionService {

    void updateUserFcmToken(UserFcmTokenMessage request);
}
