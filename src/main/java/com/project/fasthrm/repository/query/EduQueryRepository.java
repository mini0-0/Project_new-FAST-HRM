package com.project.fasthrm.repository.query;

import com.project.fasthrm.domain.Edu;
import com.project.fasthrm.dto.response.EduDto;

import java.util.List;

public interface EduQueryRepository {
    List<EduDto> findEduByPlaceId(Long placeId);
    List<EduDto> findEduByWorkerId(Long workerId);
    List<EduDto> findTodayLessons(Long placeId);
}
