package com.project.fasthrm.service;


import com.project.fasthrm.dto.response.WorkerManageDto;

import java.util.List;

public interface WorkerManageService {

    List<WorkerManageDto> findWorkerByPlaceId(Long placeId);
    void updateWorkerManage(WorkerManageDto dto);
    void deleteWorkerManage(Long userId);


}
