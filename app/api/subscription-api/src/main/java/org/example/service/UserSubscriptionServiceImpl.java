package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.conver.message.UserFcmTokenMessage;
import org.example.dto.request.UserFcmTokenDomainRequest;
import org.example.usecase.ArtistSubscriptionUseCase;
import org.example.usecase.GenreSubscriptionUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final ArtistSubscriptionUseCase artistSubscriptionUseCase;
    private final GenreSubscriptionUseCase genreSubscriptionUseCase;

    @Override
    @Transactional
    public void updateUserFcmToken(UserFcmTokenMessage request) {
        artistSubscriptionUseCase.updateUserFcmToken(
            UserFcmTokenDomainRequest.of(request.previousFcmToken(), request.updatedFcmToken()));
        genreSubscriptionUseCase.updateUserFcmToken(
            UserFcmTokenDomainRequest.of(request.previousFcmToken(), request.updatedFcmToken()));
    }
}
