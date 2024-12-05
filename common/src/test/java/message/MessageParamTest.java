package message;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.message.MessageParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MessageParamTest {

    @ParameterizedTest
    @ValueSource(strings = {"5", "10", "30"})
    @DisplayName("티켓팅 알림 시간 메시지를 분 단위 형식으로 제목을 만든다")
    void createTitleWithMinutesUnit(String minutes) {
        //given & when
        MessageParam messageParam = MessageParam.builder()
            .showTitle("showTitle")
            .minutes(minutes)
            .contents("contents")
            .build();

        //then
        assertThat(messageParam.getTitle()).contains("분 남았어요!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"60", "120", "180"})
    @DisplayName("티켓팅 알림 시간 메시지를 시 단위 형식으로 제목을 만든다")
    void createTitleWithHoursUnit(String minutes) {
        //given & when
        MessageParam messageParam = MessageParam.builder()
            .showTitle("showTitle")
            .minutes(minutes)
            .contents("contents")
            .build();

        //then
        assertThat(messageParam.getTitle()).contains("시간 남았어요!");
    }

    @Test
    @DisplayName("티켓팅 알림 시간 메시지중 5분일때 예매가 시작됨을 본문으로 만든다")
    void createBodyReservationStartAtFiveMinutes() {
        //given & when
        MessageParam messageParam = MessageParam.builder()
            .showTitle("showTitle")
            .minutes("5")
            .contents("contents")
            .build();

        //then
        assertThat(messageParam.getBody()).contains("예매가 시작됩니다!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "30", "120"})
    @DisplayName("티켓팅 알림 시간 메시지중 5분초과 일떄 예매가 오픈됨을 본문으로 만든다")
    void createBodyReservationOpenAfterFiveMinutes(String minutes) {
        //given & when
        MessageParam messageParam = MessageParam.builder()
            .showTitle("showTitle")
            .minutes(minutes)
            .contents("contents")
            .build();

        //then
        assertThat(messageParam.getBody()).contains("예매가 오픈됩니다!");
    }
}
