package com.project.fasthrm.api;

import com.project.fasthrm.dto.response.WorkerMainDto;
import com.project.fasthrm.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worker")
public class WorkerMainApi {

    private final WorkerService workerService;

    @GetMapping("/main")
    public List<WorkerMainDto> findWorkers(@RequestParam Long placeId) {
        return workerService.findWorkerByPlaceId(placeId);
    }
}
