package com.project.fasthrm.dto.response;

import com.project.fasthrm.domain.type.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberWeeklyAttendanceDto {
    private LocalDate date; // 날짜
    private String dayOfWeek; // 요일
    private AttendanceStatus isAttended; // 출석 여부(PRESENT / ABSENT / LATE)

    public MemberWeeklyAttendanceDto(LocalDate date, AttendanceStatus status) {
        this.date = date;
        this.dayOfWeek = (date != null)
                ? date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                : null;
        this.isAttended = status != null ? status : AttendanceStatus.ABSENT;
    }


}
