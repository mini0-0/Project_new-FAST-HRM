package com.project.fasthrm.service;

import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;

import java.time.LocalDate;
import java.util.List;

public interface MemberMainService {
    List<MemberTodayLessonDto> getTodayLessons(Long placeId, Long memberId, LocalDate today);
    List<MemberWeeklyAttendanceDto> getWeeklyAttendances(Long placeId, Long memberId, LocalDate today);


}
