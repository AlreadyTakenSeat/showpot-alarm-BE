package org.example.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record ArtistSubscriptionMessageDomainRequest(
    String userFcmToken,
    List<ArtistMessageDomainRequest> artists
) {

}