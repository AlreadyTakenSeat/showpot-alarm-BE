package org.example.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SubscribedMessageTemplate {

    public MessageParam getSubscribedArtistVisitKoreaAlertMessage(String artistName) {
        return new MessageParam(
            String.format("속보! %s의 내한 소식이 발표되었어요\uD83C\uDF8A", artistName),
            "쇼팟에서 상세한 내한 정보를 확인해보세요!"
        );

    }

    public MessageParam getSubscribedGenreVisitKoreaAlertMessage(String genreName) {
        return new MessageParam(
            String.format("구독하신 %s 장르의 공연 소식이 발표되었어요\uD83C\uDF88", genreName),
            "쇼팟에서 상세한 내한 정보를 확인해보세요!"
        );
    }
}
