package com.project.fasthrm.api;

import com.project.fasthrm.dto.response.WorkerManageDto;
import com.project.fasthrm.service.WorkerManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worker")
public class WorkerManageApi {

    private final WorkerManageService workerManageService;

    @GetMapping("/manage")
    public List<WorkerManageDto> findWorkers(@RequestParam Long placeId) {
        return workerManageService.findWorkerByPlaceId(placeId);
    }

    @PutMapping("/manage")
    public void updateWorkerManage(@RequestBody WorkerManageDto dto) {
        workerManageService.updateWorkerManage(dto);
    }

    @DeleteMapping("/manage")
    void deleteWorkerManage(@RequestParam Long userId) {
        workerManageService.deleteWorkerManage(userId);
    }

}
