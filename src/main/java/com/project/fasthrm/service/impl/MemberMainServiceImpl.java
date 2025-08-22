package com.project.fasthrm.service.impl;

import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;
import com.project.fasthrm.repository.query.MemberMainQueryRepository;
import com.project.fasthrm.service.MemberMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberMainServiceImpl implements MemberMainService {
    private final MemberMainQueryRepository memberMainQueryRepository;

    @Override
    public List<MemberTodayLessonDto> getTodayLessons(Long placeId, Long memberId, LocalDate today){
        return memberMainQueryRepository.findTodayLessons(placeId, memberId, today);
    }

    @Override
    public List<MemberWeeklyAttendanceDto> getWeeklyAttendances(Long placeId, Long memberId, LocalDate today) {
        return memberMainQueryRepository.findWeeklyAttendances(placeId, memberId, today);
    }


}
