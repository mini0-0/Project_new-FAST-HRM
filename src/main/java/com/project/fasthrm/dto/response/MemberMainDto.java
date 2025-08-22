package com.project.fasthrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMainDto {
    // todayLessons - member가 듣는 오늘의 수업
    private List<MemberTodayLessonDto> todayLessons;
    // WeeklyAttendance - member의 오늘 기준으로 1주일 출결현황
    private List<MemberWeeklyAttendanceDto> weeklyAttendance;

}
