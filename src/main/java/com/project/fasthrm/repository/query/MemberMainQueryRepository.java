package com.project.fasthrm.repository.query;

import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;

import java.time.LocalDate;
import java.util.List;

public interface MemberMainQueryRepository {
    List<MemberTodayLessonDto> findTodayLessons(Long placeId, Long memberId, LocalDate today);
    List<MemberWeeklyAttendanceDto> findWeeklyAttendances(Long placeId, Long memberId, LocalDate today);
}
