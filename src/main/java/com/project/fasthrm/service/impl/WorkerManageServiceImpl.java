package com.project.fasthrm.service.impl;

import com.project.fasthrm.dto.response.WorkerManageDto;
import com.project.fasthrm.repository.query.WorkerQueryRepository;
import com.project.fasthrm.service.WorkerManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerManageServiceImpl implements WorkerManageService {

    private final WorkerQueryRepository workerQueryRepository;

    @Override
    public List<WorkerManageDto> findWorkerByPlaceId(Long placeId) {
        return workerQueryRepository.findWorkerByPlaceId(placeId);
    }

    @Override
    public void updateWorkerManage(WorkerManageDto dto) {
        workerQueryRepository.updateWorkerManage(dto);
    }

    @Override
    public void deleteWorkerManage(Long userId) {
        workerQueryRepository.deleteWorkerManage(userId);
    }


}
