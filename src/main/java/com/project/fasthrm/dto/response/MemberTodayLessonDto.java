package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MemberTodayLessonDto {
    // 오늘 날짜
    private LocalDate date;
    // 오늘 요일
    private String dayOfWeek;
    // 수업명
    private String eduName;
    // 수업 시작 시간
    private LocalDateTime eduStart;
    // 수업 종료 시간
    private LocalDateTime eduEnd;
    // 강사 이름
    private String workerName;
    // 강의실 정보
    private String eduRoomName;

    @QueryProjection
    public MemberTodayLessonDto(LocalDate date, String dayOfWeek, String eduName,
                                LocalDateTime eduStart, LocalDateTime eduEnd,
                                String workerName, String eduRoomName) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.eduName = eduName;
        this.eduStart = eduStart;
        this.eduEnd = eduEnd;
        this.workerName = workerName;
        this.eduRoomName = eduRoomName;

    }

}
