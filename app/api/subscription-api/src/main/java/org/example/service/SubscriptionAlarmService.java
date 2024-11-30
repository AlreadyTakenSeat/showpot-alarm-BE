package org.example.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.SubscriptionMessage;
import org.example.dto.response.ArtistSubscriptionDomainResponse;
import org.example.dto.response.GenreSubscriptionDomainResponse;
import org.example.entity.ShowAlarm;
import org.example.message.MessageParam;
import org.example.message.SubscribedMessageTemplate;
import org.example.service.dto.ArtistSubscriptionMessageServiceRequest;
import org.example.service.dto.GenreSubscriptionMessageServiceRequest;
import org.example.service.dto.MultipleTargetMessageServiceRequest;
import org.example.service.dto.SubscriptionMessageServiceRequest;
import org.example.usecase.ArtistSubscriptionUseCase;
import org.example.usecase.GenreSubscriptionUseCase;
import org.example.usecase.ShowAlarmUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionAlarmService {

    private final ArtistSubscriptionUseCase artistSubscriptionUseCase;
    private final GenreSubscriptionUseCase genreSubscriptionUseCase;
    private final ShowAlarmUseCase showAlarmUseCase;
    private final SubscriptionMessage subscriptionMessage;

    public void showRelationSubscription(SubscriptionMessageServiceRequest request) {
        var artistSubscriptions = artistSubscriptionUseCase.findArtistSubscriptionsByArtistIds(request.artistIds());
        Map<String, List<String>> artistSubscriptionsMap = artistSubscriptions.stream()
            .collect(Collectors.groupingBy(
                ArtistSubscriptionDomainResponse::artistName,
                Collectors.mapping(
                    ArtistSubscriptionDomainResponse::userFcmToken,
                    Collectors.toList()
                )
            ));
        for (Map.Entry<String, List<String>> entry : artistSubscriptionsMap.entrySet()) {
            String artistName = entry.getKey();
            List<String> fcmTokens = entry.getValue();
            MessageParam message = SubscribedMessageTemplate.getSubscribedArtistVisitKoreaAlertMessage(artistName);

            subscriptionMessage.send(MultipleTargetMessageServiceRequest.of(fcmTokens, message));
            saveShowAlarm(request.showId(), fcmTokens, message);
        }

        var genreSubscriptions = genreSubscriptionUseCase.findGenreSubscriptionsByGenreIds(request.genreIds());
        Map<String, List<String>> genreSubscriptionsMap = genreSubscriptions.stream()
            .collect(Collectors.groupingBy(
                GenreSubscriptionDomainResponse::genreName,
                Collectors.mapping(
                    GenreSubscriptionDomainResponse::userFcmToken,
                    Collectors.toList()
                )
            ));
        for (Map.Entry<String, List<String>> entry : genreSubscriptionsMap.entrySet()) {
            String genreName = entry.getKey();
            List<String> fcmTokens = entry.getValue();
            MessageParam message = SubscribedMessageTemplate.getSubscribedGenreVisitKoreaAlertMessage(genreName);

            subscriptionMessage.send(MultipleTargetMessageServiceRequest.of(fcmTokens, message));
            saveShowAlarm(request.showId(), fcmTokens, message);
        }
    }

    @Transactional
    public void saveShowAlarm(UUID showId, List<String> fcmTokens, MessageParam message) {
        fcmTokens.stream()
            .map(userFcmToken -> ShowAlarm.builder()
                .showId(showId)
                .userFcmToken(userFcmToken)
                .title(message.getTitle())
                .content(message.getBody())
                .checked(false)
                .build()
            )
            .forEach(showAlarmUseCase::save);
    }

    public void artistSubscribe(ArtistSubscriptionMessageServiceRequest request) {
        artistSubscriptionUseCase.artistSubscribe(request.toDomainRequest());
    }

    public void artistUnsubscribe(ArtistSubscriptionMessageServiceRequest request) {
        artistSubscriptionUseCase.artistUnsubscribe(request.toDomainRequest());
    }

    public void genreSubscribe(GenreSubscriptionMessageServiceRequest request) {
        genreSubscriptionUseCase.genreSubscribe(request.toDomainRequest());
    }

    public void genreUnsubscribe(GenreSubscriptionMessageServiceRequest request) {
        genreSubscriptionUseCase.genreUnsubscribe(request.toDomainRequest());
    }
}
