package com.project.fasthrm.service;


import com.project.fasthrm.dto.response.WorkerMainDto;

import java.util.List;

public interface WorkerService {

    List<WorkerMainDto> findWorkerByPlaceId(Long placeId);

}
