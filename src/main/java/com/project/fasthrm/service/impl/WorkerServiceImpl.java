package com.project.fasthrm.service.impl;

import com.project.fasthrm.dto.response.WorkerMainDto;
import com.project.fasthrm.repository.query.WorkerQueryRepository;
import com.project.fasthrm.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerQueryRepository workerQueryRepository;

    @Override
    public List<WorkerMainDto> findWorkerByPlaceId(Long placeId) {

        return workerQueryRepository.findWorkerByPlaceId(placeId);
    }

}
