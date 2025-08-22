package com.project.fasthrm.repository.query;

import com.project.fasthrm.domain.QEdu;
import com.project.fasthrm.domain.QWorker;
import com.project.fasthrm.dto.response.EduDto;
import com.project.fasthrm.dto.response.QEduDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EduQueryRepositoryImpl implements EduQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final QEdu edu = new QEdu("edu");
    private final QWorker worker = new QWorker("worker");


    @Override
    public List<EduDto> findEduByPlaceId(Long placeId) {
        return queryFactory
                .select(new QEduDto(
                        edu.eduName,
                        edu.eduDay,
                        edu.eduStart,
                        edu.eduEnd,
                        edu.eduTuition,
                        edu.worker.approvedForTuitionUpdate,
                        edu.eduRoomName
                ))
                .from(edu)
                .where(edu.place.id.eq(placeId))
                .fetch();
    }

    @Override
    public List<EduDto> findEduByWorkerId(Long workerId) {
        return queryFactory
                .select(new QEduDto(
                        edu.eduName,
                        edu.eduDay,
                        edu.eduStart,
                        edu.eduEnd,
                        edu.eduTuition,
                        edu.worker.approvedForTuitionUpdate,
                        edu.eduRoomName
                ))
                .from(edu)
                .where(edu.worker.id.eq(workerId))
                .fetch();
    }

    @Override
    public List<EduDto> findTodayLessons(Long placeId) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        String dayStr = switch (today) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };

        return queryFactory
                .select(new QEduDto(
                        edu.eduName,
                        edu.eduDay,
                        edu.eduStart,
                        edu.eduEnd,
                        edu.eduTuition,
                        edu.worker.approvedForTuitionUpdate,
                        edu.eduRoomName
                ))
                .from(edu)
                .where(edu.place.id.eq(placeId)
                        .and(edu.eduDay.contains(dayStr).or(edu.eduDay.eq("매일"))))
                .fetch();
    }
}
