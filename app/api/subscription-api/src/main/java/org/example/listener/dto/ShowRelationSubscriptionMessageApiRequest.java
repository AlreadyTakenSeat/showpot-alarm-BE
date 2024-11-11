package org.example.listener.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.example.service.dto.SubscriptionMessageServiceRequest;

@Builder
public record ShowRelationSubscriptionMessageApiRequest(
    UUID showId,
    List<UUID> artistIds,
    List<UUID> genreIds
) {

    public SubscriptionMessageServiceRequest toServiceRequest() {
        return SubscriptionMessageServiceRequest.builder()
            .showId(showId)
            .artistIds(artistIds)
            .genreIds(genreIds)
            .build();
    }
}
