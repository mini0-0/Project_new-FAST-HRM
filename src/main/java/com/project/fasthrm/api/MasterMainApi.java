package com.project.fasthrm.api;


import com.project.fasthrm.dto.response.MasterMainDto;
import com.project.fasthrm.service.MasterMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/masters")
public class MasterMainApi {

    private final MasterMainService masterMainService;

    @GetMapping("main")
    public ResponseEntity<MasterMainDto> getMasterMainDashboard(@RequestParam Long placeId) {
        return ResponseEntity.ok(masterMainService.getMasterMainDashboard(placeId));
    }

}
