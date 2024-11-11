package org.example.repository.alarm;

import static org.example.entity.QShowAlarm.showAlarm;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                    showAlarm.title,
                    showAlarm.content,
                    showAlarm.createdAt,
                    showAlarm.checked
                )
            )
            .from(showAlarm)
            .where(getDefaultPredicateInCursorPagination(request.cursorId()))
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

    private Predicate getDefaultPredicateInCursorPagination(UUID cursor) {
        BooleanExpression defaultPredicate = showAlarm.isDeleted.isFalse();

        return cursor == null ? defaultPredicate : showAlarm.id.gt(cursor).and(defaultPredicate);
    }
}
