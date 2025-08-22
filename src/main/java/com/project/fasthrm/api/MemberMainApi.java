package com.project.fasthrm.api;

import com.project.fasthrm.dto.response.MemberMainDto;
import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;
import com.project.fasthrm.service.MemberMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberMainApi {

    private final MemberMainService memberMainService;

    @GetMapping("/main")
    public ResponseEntity<MemberMainDto> getMemberMainPage(
            @RequestParam Long placeId,
            @RequestParam Long memberId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate today
    ) {
        if (today == null) {
            today = LocalDate.now();
        }

        List<MemberTodayLessonDto> todayLessons =
                memberMainService.getTodayLessons(placeId, memberId, today);
        List<MemberWeeklyAttendanceDto> weeklyAttendance =
                memberMainService.getWeeklyAttendances(placeId, memberId, today);

        return ResponseEntity.ok(new MemberMainDto(todayLessons, weeklyAttendance));
    }
}
