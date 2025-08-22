package com.project.fasthrm.repository.query;

import com.project.fasthrm.domain.*;
import com.project.fasthrm.domain.type.AttendanceStatus;
import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;
import com.project.fasthrm.dto.response.QMemberTodayLessonDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
@RequiredArgsConstructor
public class MemberMainQueryRepositoryImpl implements MemberMainQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QEdu edu = new QEdu("edu");
    private final QTakes takes = new QTakes("takes");
    private final QMember member = new QMember("member");
    private final QUser user = new QUser("user");
    private final QAttendance attendance = new QAttendance("attendance");
    private final QWorker worker = new QWorker("worker");


    @Override
    public List<MemberTodayLessonDto> findTodayLessons(Long placeId, Long memberId, LocalDate today) {
        // 오늘 요일
        String dayOfWeek = today.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        // 이번 달 범위
        LocalDate startOfMonth    = today.withDayOfMonth(1);
        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

        return queryFactory
                .select(new QMemberTodayLessonDto(
                        Expressions.constant(today),        // date
                        Expressions.constant(dayOfWeek),    // dayOfWeek
                        edu.eduName,                        // eduName
                        edu.eduStart,                       // eduStart
                        edu.eduEnd,                         // eduEnd
                        user.userRealName,            // workerName
                        edu.eduRoomName                     // eduRoomName
                ))
                .from(takes)
                .join(takes.edu, edu)
                .join(edu.worker, worker)
                .join(worker.user, user)
                .where(
                        takes.member.id.eq(memberId),
                        edu.place.id.eq(placeId),
                        edu.eduDay.contains(dayOfWeek),
                        // 등록일이 이번 달
                        takes.registeredAt.goe(startOfMonth),
                        takes.registeredAt.lt(startOfNextMonth)
                )
                .orderBy(takes.registeredAt.asc())
                .limit(10)
                .fetch();

    }

    @Override
    public List<MemberWeeklyAttendanceDto> findWeeklyAttendances(Long placeId, Long memberId, LocalDate today) {
        // 이번주 월-일 범위
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // 해당 주의 출석 데이터 조회
        List<LocalDate> attendedDates = queryFactory
                .select(takes.registeredAt)
                .from(takes)
                .join(takes.edu, edu)
                .where(
                        takes.member.id.eq(memberId),
                        edu.place.id.eq(placeId),
                        takes.registeredAt.between(startOfWeek, endOfWeek)
                )
                .fetch();


        return startOfWeek.datesUntil(endOfWeek.plusDays(1))
                .map(date -> {
                    AttendanceStatus status = attendedDates.contains(date)
                            ? AttendanceStatus.PRESENT
                            : AttendanceStatus.ABSENT;
                    return new MemberWeeklyAttendanceDto(date, status);
                })
                .toList();
    }

}