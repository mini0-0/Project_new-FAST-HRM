package com.project.fasthrm.repository.query;

import com.project.fasthrm.dto.response.WorkerManageDto;

import java.util.List;

public interface WorkerQueryRepository {

    List<WorkerManageDto> findWorkerByPlaceId(Long placeId);
    void updateWorkerManage(WorkerManageDto dto);
    void deleteWorkerManage(Long userId);

}
