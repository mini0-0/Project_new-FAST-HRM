package com.project.fasthrm.service.impl;

import com.project.fasthrm.domain.Edu;
import com.project.fasthrm.dto.response.EduDto;
import com.project.fasthrm.repository.query.EduQueryRepository;
import com.project.fasthrm.repository.EduRepository;
import com.project.fasthrm.service.WorkerMainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WorkerMainServiceImpl implements WorkerMainService {

    private final EduRepository eduRepository;
    private final EduQueryRepository eduQueryRepository;

    @Override
    public List<EduDto> getEduListByPlaceId(Long placeId) {
        return eduQueryRepository.findEduByPlaceId(placeId);
    }

    @Override
    public List<EduDto> getEduListWorkerId(Long workerId) {
        return eduQueryRepository.findEduByWorkerId(workerId);
    }

    @Override
    public List<EduDto> getTodayLessons(Long placeId) {
        return eduQueryRepository.findTodayLessons(placeId);
    }

    @Override
    public void createLesson(String role,String approved ,EduDto dto) {
        if (role.equals("WORKER") && (dto.getEduTuition() != null && !dto.isApprovedForTuitionUpdate())) {
            throw new SecurityException("승인되지 않은 WORKER는 가격을 입력할 수 없습니다.");
        }

        Edu edu = dto.toEntity(); // Edu 엔티티 생성
        eduRepository.save(edu);
    }

    @Override
    public void updateLesson(Long eduId, String role, EduDto dto) {
        Edu edu = eduRepository.findById(eduId)
                .orElseThrow(() -> new IllegalArgumentException("수업이 존재하지 않습니다."));

        if (role.equals("WORKER") && (dto.getEduTuition() != null && !dto.isApprovedForTuitionUpdate())) {
            throw new SecurityException("승인되지 않은 WORKER는 가격을 수정할 수 없습니다.");
        }

        edu.updateFromDto(dto); // Edu 엔티티에 정의된 수정 메서드 호출
    }

    @Override
    public void deleteLesson(Long eduId, String role) {
        if (!role.equals("MASTER")) {
            throw new SecurityException("MASTER만 삭제할 수 있습니다.");
        }

        eduRepository.deleteById(eduId);
    }

}
