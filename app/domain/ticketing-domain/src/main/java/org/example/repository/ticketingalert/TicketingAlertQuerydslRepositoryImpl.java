
package org.example.repository.ticketingalert;

import static org.example.entity.QTicketingAlert.ticketingAlert;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TicketingAlert;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketingAlertQuerydslRepositoryImpl implements
    TicketingAlertQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<TicketingAlert> findAllByFcmTokenAndAlertTimeAfterNow(String fcmToken, LocalDateTime now) {
        return jpaQueryFactory.selectFrom(ticketingAlert)
            .where(ticketingAlert.userFcmToken.eq(fcmToken)
                .and(ticketingAlert.alertTime.after(now))
                .and(ticketingAlert.isDeleted.isFalse()))
            .stream().toList();
    }
}
