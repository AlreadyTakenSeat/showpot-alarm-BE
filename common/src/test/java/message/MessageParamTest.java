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
    @DisplayName("티켓팅_알림_시간_메시지를_분_단위_형식으로_제목을_만든다")
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
    @DisplayName("티켓팅_알림_시간_메시지를_시_단위_형식으로_제목을_만든다")
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
    @DisplayName("티켓팅_알림_시간_메시지중_5분일때_예매가_시작됨을_본문으로_만든다")
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
    @DisplayName("티켓팅_알림_시간_메시지중_5분초과_일떄_예매가_오픈됨을_본문으로_만든다")
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
