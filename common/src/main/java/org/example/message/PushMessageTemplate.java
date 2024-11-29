package org.example.message;

public class PushMessageTemplate {

    public static MessageParam getTicketingAlertMessageBeforeHours(String showTitle, String minutes) {
        switch (minutes) {
            case "5" -> {
                return getTicketingAlertMessageBefore5Minutes(showTitle, minutes);
            }
            case "10" -> {
                return getTicketingAlertMessageBefore10Minutes(showTitle, minutes);
            }
            case "30" -> {
                return getTicketingAlertMessageBefore30Minutes(showTitle, minutes);
            }
            case "60" -> {
                return getTicketingAlertMessageBefore60Minutes(showTitle, minutes);
            }
            default -> {
                throw new IllegalArgumentException("Unsupported minutes: " + minutes);
            }
        }
    }

    public static MessageParam getTicketingAlertMessageBefore5Minutes(String showTitle, String minutes) {
        return MessageParam.builder()
            .title(showTitle + " 티켓팅이 " + minutes + "분 남았어요!")
            .body(String.format(minutes + "분 후, " + showTitle + "예매가 시작됩니다!\n티켓팅을 잊지 말고 준비하세요🎟"))
            .build();
    }

    public static MessageParam getTicketingAlertMessageBefore10Minutes(String showTitle, String minutes) {
        return MessageParam.builder()
            .title(showTitle + " 티켓팅이 " + minutes + "분 남았어요!")
            .body(String.format(minutes + "분 후, " + showTitle + "예매가 오픈됩니다!\n성공적인 티켓팅을 쇼팟이 응원해요🥰"))
            .build();
    }

    public static MessageParam getTicketingAlertMessageBefore30Minutes(String showTitle, String minutes) {
        return MessageParam.builder()
            .title(showTitle + " 티켓팅이 " + minutes + "분 남았어요!")
            .body(String.format(minutes + "분 후, " + showTitle + "예매가 오픈됩니다.\n놓치지 말고 티켓팅을 준비하세요😀"))
            .build();
    }

    public static MessageParam getTicketingAlertMessageBefore60Minutes(String showTitle, String minutes) {
        return MessageParam.builder()
            .title(showTitle + " 티켓팅이 " + 1 + "시간 남았어요!")
            .body(String.format(minutes + "시간 후, " + showTitle + "예매가 오픈됩니다.\n놓치지 말고 티켓팅을 준비하세요😀"))
            .build();
    }

    public static MessageParam getSubscribedArtistVisitKoreaAlertMessage(String artistName) {
        return MessageParam.builder()
            .title(
                String.format("속보! %s의 내한 소식이 발표되었어요\uD83C\uDF8A", artistName)
            )
            .body("쇼팟에서 상세한 내한 정보를 확인해보세요!")
            .build();
    }

    public static MessageParam getSubscribedGenreVisitKoreaAlertMessage(String genreName) {
        return MessageParam.builder()
            .title(
                String.format("구독하신 %s 장르의 공연 소식이 발표되었어요\uD83C\uDF88", genreName)
            )
            .body("쇼팟에서 상세한 공연 정보를 확인해보세요!")
            .build();
    }
}
