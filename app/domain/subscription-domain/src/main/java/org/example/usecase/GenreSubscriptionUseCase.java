package org.example.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.GenreMessageDomainRequest;
import org.example.dto.request.GenreSubscriptionMessageDomainRequest;
import org.example.dto.request.UserFcmTokenDomainRequest;
import org.example.dto.response.GenreSubscriptionDomainResponse;
import org.example.entity.GenreSubscription;
import org.example.repository.subscription.genresubscription.GenreSubscriptionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GenreSubscriptionUseCase {

    private final GenreSubscriptionRepository genreSubscriptionRepository;

    public List<GenreSubscriptionDomainResponse> findGenreSubscriptionsByGenreIds(
        List<UUID> genreIds) {
        return genreSubscriptionRepository.findGenreSubscriptionsByGenreIds(genreIds);
    }

    @Transactional
    public void genreSubscribe(GenreSubscriptionMessageDomainRequest request) {
        var newSubscriptions = new ArrayList<GenreSubscription>();
        var allSubscriptionByGenreId = genreSubscriptionRepository
            .findAllByUserFcmToken(request.userFcmToken())
            .stream()
            .collect(Collectors.toMap(GenreSubscription::getGenreId, it -> it));

        for (var genre : request.genres()) {
            if (allSubscriptionByGenreId.containsKey(genre.genreId())) {
                var existSubscription = allSubscriptionByGenreId.get(genre.genreId());
                existSubscription.subscribe();
                continue;
            }

            newSubscriptions.add(GenreSubscription.builder()
                .userFcmToken(request.userFcmToken())
                .genreId(genre.genreId())
                .genreName(genre.genreName())
                .build()
            );
        }

        genreSubscriptionRepository.saveAll(newSubscriptions);
    }

    @Transactional
    public void genreUnsubscribe(GenreSubscriptionMessageDomainRequest request) {
        var genreIds = request.genres().stream()
            .map(GenreMessageDomainRequest::genreId)
            .collect(Collectors.toSet());

        var artistSubscriptions = genreSubscriptionRepository.findSubscriptionList(
            request.userFcmToken());
        var filteredSubscription = artistSubscriptions.stream()
            .filter(it -> genreIds.contains(it.getGenreId()))
            .toList();

        filteredSubscription.forEach(GenreSubscription::unsubscribe);
    }

    public void updateUserFcmToken(UserFcmTokenDomainRequest request) {
        List<GenreSubscription> genreSubscriptions = genreSubscriptionRepository.findAllByUserFcmToken(
            request.previousFcmToken());

        genreSubscriptions.forEach(genreSubscription -> {
            genreSubscription.updateUserFcmToken(request.updatedFcmToken());
            genreSubscriptionRepository.save(genreSubscription);
        });
    }
}
