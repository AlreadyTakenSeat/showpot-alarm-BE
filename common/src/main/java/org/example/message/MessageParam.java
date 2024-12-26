package org.example.message;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageParam {

    private final String title;
    private final String body;


    public MessageParam(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Builder
    public MessageParam(String showTitle, String minutes, String contents) {
        this.title = createTitle(showTitle, minutes);
        this.body = createBodyPrefix(minutes) + showTitle + createBodySuffix(minutes) + contents;
    }

    private String createTitle(String showTitle, String minutes) {
        StringBuilder title = new StringBuilder();
        title.append(showTitle).append(" 티켓팅이 ");

        if (isHourUnit(minutes)) {
            title.append(getHour(minutes)).append("시간 남았어요!");
            return title.toString();
        }

        return title.append(minutes).append("분 남았어요!").toString();
    }

    private String createBodyPrefix(String minutes) {
        StringBuilder prefix = new StringBuilder();
        if (isHourUnit(minutes)) {
            return prefix.append(getHour(minutes)).append("시간 후, ").toString();
        }

        return prefix.append(minutes).append("분 후, ").toString();
    }

    private String createBodySuffix(String minutes) {
        StringBuilder suffix = new StringBuilder();

        if (minutes.equals("5")) {
            suffix.append(" 예매가 시작됩니다! ");
            return suffix.toString();
        }

        return suffix.append(" 예매가 오픈됩니다! ").toString();
    }

    private boolean isHourUnit(String minutes) {
        return Integer.parseInt(minutes) % 60 == 0;
    }

    private static int getHour(String minutes) {
        return Integer.parseInt(minutes) / 60;
    }
}
