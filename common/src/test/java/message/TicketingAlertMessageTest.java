package message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.example.message.MessageParam;
import org.example.message.TicketingAlertMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TicketingAlertMessageTest {

    @ParameterizedTest
    @ValueSource(strings = {"5", "10", "30"})
    @DisplayName("알림 메시지를 분 단위로 생성한다")
    void getTicketingAlertMessageByMinutes(String minutes) {
        MessageParam message = TicketingAlertMessage.getTicketingAlertMessage("showTitle", minutes);

        assertThat(message).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"4", "13", "25", "66"})
    @DisplayName("일치하지 않는 분 단위는 예외가 발생한다")
    void ticketingAlertMessageNotEqualMinutesThrowException(String minutes) {
        assertThatThrownBy(
            () -> TicketingAlertMessage.getTicketingAlertMessage("showTitle", minutes)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}