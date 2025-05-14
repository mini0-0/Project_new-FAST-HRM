package com.project.fasthrm.api;

import com.project.fasthrm.dto.response.EduDto;
import com.project.fasthrm.service.WorkerMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workers/main")
public class WorkerMainApi {

    private final WorkerMainService workerMainService;

    // 전체 수업 조회
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<EduDto>> getEduListByPlaceId(@PathVariable Long placeId) {
        return ResponseEntity.ok(workerMainService.getEduListByPlaceId(placeId));

    }

    // worker별 수업 조회
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<EduDto>> getEduListByWorkerId(@PathVariable Long workerId) {
        return ResponseEntity.ok(workerMainService.getEduListWorkerId(workerId));

    }

    // 오늘의 수업 조회
    @GetMapping("/today_lesson")
    public ResponseEntity<List<EduDto>> getTodayLessons(@RequestParam Long placeId) {
        return ResponseEntity.ok(workerMainService.getTodayLessons(placeId));
    }

    // 수업 추가
    @PostMapping("/lesson")
    public ResponseEntity<?> createLesson(@RequestHeader("Role") String role,
                                             @RequestHeader(value = "Approved", required = false) String approved,
                                             @RequestBody EduDto dto) {
        try {
            workerMainService.createLesson(role, approved, dto);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            // SecurityException이 발생하면 403(Forbidden) 상태 코드 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // 수업 수정
    @PutMapping("/lesson/{eduId}")
    public ResponseEntity<Void> updateLesson(@PathVariable Long eduId,
                                             @RequestHeader("Role") String role,
                                             @RequestBody EduDto dto) {
        workerMainService.updateLesson(eduId, role, dto);
        return ResponseEntity.ok().build();
    }

    // 수업 삭제
    @DeleteMapping("/lesson/{eduId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long eduId,
                                             @RequestHeader("Role") String role) {
        workerMainService.deleteLesson(eduId, role);
        return ResponseEntity.ok().build();
    }
}
