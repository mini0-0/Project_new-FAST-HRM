package com.project.fasthrm.service;

import com.project.fasthrm.dto.response.EduDto;

import java.util.List;

public interface WorkerMainService {

    List<EduDto> getEduListByPlaceId(Long placeId);
    List<EduDto> getEduListWorkerId(Long workerId);
    List<EduDto> getTodayLessons(Long placeId);
    void createLesson(String role, String approved, EduDto dto);
    void updateLesson(Long eduId, String role, EduDto dto);
    void deleteLesson(Long eduId, String role);


}