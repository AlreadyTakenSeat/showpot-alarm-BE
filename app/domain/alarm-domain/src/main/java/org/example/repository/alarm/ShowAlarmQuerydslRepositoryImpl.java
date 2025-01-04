package org.example.repository.alarm;

import static org.example.entity.QShowAlarm.showAlarm;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ShowAlarmsDomainRequest;
import org.example.dto.response.ShowAlarmDomainResponse;
import org.example.dto.response.ShowAlarmPaginationDomainResponse;
import org.example.util.SliceUtil;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowAlarmQuerydslRepositoryImpl implements ShowAlarmQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ShowAlarmPaginationDomainResponse findAllWithCursorPagination(
        ShowAlarmsDomainRequest request
    ) {
        List<ShowAlarmDomainResponse> response = jpaQueryFactory.select(
                Projections.constructor(
                    ShowAlarmDomainResponse.class,
                    showAlarm.id,
                    showAlarm.showId,
                    showAlarm.title,
                    showAlarm.content,
                    showAlarm.createdAt,
                    showAlarm.checked
                )
            )
            .from(showAlarm)
            .where(getDefaultPredicateInCursorPagination(request.cursorId(), request.cursorValue()))
            .orderBy(
                showAlarm.createdAt.desc(),
                showAlarm.id.asc()
            )
            .limit(request.size() + 1)
            .fetch();

        Slice<ShowAlarmDomainResponse> responseSlice = SliceUtil.makeSlice(
            request.size(),
            response
        );

        return ShowAlarmPaginationDomainResponse.builder()
            .data(responseSlice.getContent())
            .hasNext(responseSlice.hasNext())
            .build();
    }

    private Predicate getDefaultPredicateInCursorPagination(
        UUID cursorId,
        LocalDateTime cursorValue
    ) {
        BooleanExpression wherePredicate = showAlarm.isDeleted.isFalse();
        if (cursorId != null && cursorValue != null) {
            wherePredicate = wherePredicate.and(
                showAlarm.createdAt.lt(cursorValue)
                    .or(showAlarm.createdAt.eq(cursorValue).and(showAlarm.id.gt(cursorId)))

            );
        }

        return wherePredicate;
    }
}
