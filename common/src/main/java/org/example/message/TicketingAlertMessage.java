package org.example.message;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum TicketingAlertMessage {

    BEFORE_5_MINUTES("5") {

        @Override
        public MessageParam getTicketingAlertMessageBeforeMinutes(String showTitle) {
            return MessageParam.builder()
                .showTitle(showTitle)
                .minutes(getMinutes())
                .contents("티켓팅을 잊지 말고 준비하세요🎟")
                .build();
        }
    },

    BEFORE_10_MINUTES("10") {

        @Override
        public MessageParam getTicketingAlertMessageBeforeMinutes(String showTitle) {
            return MessageParam.builder()
                .showTitle(showTitle)
                .minutes(getMinutes())
                .contents("성공적인 티켓팅을 쇼팟이 응원해요🥰")
                .build();
        }
    },
    BEFORE_30_MINUTES("30") {

        @Override
        public MessageParam getTicketingAlertMessageBeforeMinutes(String showTitle) {
            return MessageParam.builder()
                .showTitle(showTitle)
                .minutes(getMinutes())
                .contents("놓치지 말고 티켓팅을 준비하세요😀")
                .build();
        }
    },
    BEFORE_60_MINUTES("60") {
        @Override
        public MessageParam getTicketingAlertMessageBeforeMinutes(String showTitle) {
            return MessageParam.builder()
                .showTitle(showTitle)
                .minutes(getMinutes())
                .contents("놓치지 말고 티켓팅을 준비하세요😀")
                .build();
        }
    };

    private final String minutes;

    TicketingAlertMessage(String minutes) {
        this.minutes = minutes;
    }

    public abstract MessageParam getTicketingAlertMessageBeforeMinutes(String title);

    public static MessageParam getTicketingAlertMessage(String showTitle, String minutes) {
        return Arrays.stream(TicketingAlertMessage.values())
            .filter(t -> t.minutes.equals(minutes))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .getTicketingAlertMessageBeforeMinutes(showTitle);
    }
}
