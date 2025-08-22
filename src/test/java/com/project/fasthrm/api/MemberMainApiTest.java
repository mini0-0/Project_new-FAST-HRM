package com.project.fasthrm.api;

import com.project.fasthrm.domain.type.AttendanceStatus;
import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;
import com.project.fasthrm.service.MemberMainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberMainApi.class)
@DisplayName("API: MemberMain")
public class MemberMainApiTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MemberMainService memberMainService;

    private final Long placeId = 1L;
    private final Long memberId = 5L;
    private final LocalDate testDate = LocalDate.of(2025, 4, 17);

    @Test
    @DisplayName("/api/members/main - 성공 확인")
    void givenPlaceAndMemberAndToday_whenRequestingMemberMain_thenReturnOkJson() throws Exception {
        // given
        when(memberMainService.getTodayLessons(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(List.of());
        when(memberMainService.getWeeklyAttendances(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(List.of());

        // when & then
        mvc.perform(get("/api/members/main")
                        .param("placeId", placeId.toString())
                        .param("memberId", memberId.toString())
                        .param("today", testDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("/api/members/main - 오늘 수업 목록 반환")
    void givenPlaceAndMemberAndToday_whenRequestingMemberMain_thenReturnTodayLessonsList() throws Exception {
        // given
        List<MemberTodayLessonDto> mockTodayLessons = List.of(
                MemberTodayLessonDto.builder()
                        .date(testDate)
                        .dayOfWeek("목")
                        .eduName("수학")
                        .eduStart(LocalDateTime.of(2025, 4, 17, 10, 0))
                        .eduEnd(LocalDateTime.of(2025, 4, 17, 11, 0))
                        .workerName("김강사")
                        .eduRoomName("101호")
                        .build()
        );
        when(memberMainService.getTodayLessons(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(mockTodayLessons);
        when(memberMainService.getWeeklyAttendances(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(List.of());

        // when & then
        mvc.perform(get("/api/members/main")
                        .param("placeId", placeId.toString())
                        .param("memberId", memberId.toString())
                        .param("today", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todayLessons[0].eduName").value("수학"));

    }


    @Test
    @DisplayName("/api/members/main - 이번주(일주일치) 출결 현황")
    void givenPlaceAndMemberAndToday_whenRequestingMemberMain_thenReturnWeeklyAttendancesList() throws Exception {
        // given
        List<MemberWeeklyAttendanceDto> weeklyAttendance = List.of(
                MemberWeeklyAttendanceDto.builder()
                        .date(LocalDate.of(2025, 4, 14)) // 월
                        .dayOfWeek("월")
                        .isAttended(AttendanceStatus.PRESENT)
                        .build(),
                MemberWeeklyAttendanceDto.builder()
                        .date(LocalDate.of(2025, 4, 15)) // 화
                        .dayOfWeek("화")
                        .isAttended(AttendanceStatus.ABSENT)
                        .build(),
                MemberWeeklyAttendanceDto.builder()
                        .date(LocalDate.of(2025, 4, 16)) // 수
                        .dayOfWeek("수")
                        .isAttended(AttendanceStatus.LATE)
                        .build()
        );

        when(memberMainService.getTodayLessons(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(List.of());
        when(memberMainService.getWeeklyAttendances(eq(placeId), eq(memberId), eq(testDate)))
                .thenReturn(weeklyAttendance);

        // when & then
        mvc.perform(get("/api/members/main")
                    .param("placeId", placeId.toString())
                    .param("memberId", memberId.toString())
                    .param("today", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyAttendance[0].dayOfWeek").value("월"))
                .andExpect(jsonPath("$.weeklyAttendance[1].isAttended").value("ABSENT"))
                .andExpect(jsonPath("$.weeklyAttendance[2].isAttended").value("LATE"));

    }
}
