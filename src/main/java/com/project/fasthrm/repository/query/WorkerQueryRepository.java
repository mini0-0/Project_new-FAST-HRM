package com.project.fasthrm.repository.query;

import com.project.fasthrm.dto.response.WorkerMainDto;

import java.util.List;

public interface WorkerQueryRepository {

    List<WorkerMainDto> findWorkerByPlaceId(Long placeId);

}
