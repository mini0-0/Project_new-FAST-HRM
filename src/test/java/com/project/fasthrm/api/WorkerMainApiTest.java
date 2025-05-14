package com.project.fasthrm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fasthrm.dto.response.EduDto;
import com.project.fasthrm.service.WorkerMainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkerMainApi.class)
@DisplayName("API: Worker Main")
public class WorkerMainApiTest {

    @Autowired private MockMvc mvc;
    @MockitoBean private WorkerMainService workerMainService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("place별 전체 수업 조회")
    void givenPlaceId_whenRequestingLessons_thenReturnLessonList() throws Exception {
        Long placeId = 1L;
        List<EduDto> lessons = List.of(
                new EduDto("Math", "월수금", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(10000), false),
                new EduDto("English", "화목", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(15000), false)
        );

        given(workerMainService.getEduListByPlaceId(placeId)).willReturn(lessons);

        mvc.perform(get("/api/workers/main/place/{placeId}", placeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].eduName").value("Math"));
    }

    @Test
    @DisplayName("worker별 수업 조회")
    void givenWorkerId_whenRequestingLessons_thenReturnList() throws Exception {
        Long workerId = 1L;

        given(workerMainService.getEduListWorkerId(workerId)).willReturn(
                List.of(new EduDto("Science", "매일", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(10000), false))
        );

        mvc.perform(get("/api/workers/main/worker/{workerId}", workerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eduName").value("Science"));
    }

    @Test
    @DisplayName("오늘 수업 조회")
    void givenPlaceId_whenRequestingTodayLessons_thenReturnTodayList() throws Exception {
        Long placeId = 1L;

        given(workerMainService.getTodayLessons(placeId)).willReturn(
                List.of(
                        new EduDto("Math", "월수금", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(12000), false),
                        new EduDto("Science", "매일", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(13000), false)
                )
        );

        mvc.perform(get("/api/workers/main/today_lesson")
                        .param("placeId", placeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("수업 추가 - MASTER 허용")
    void givenMasterLessonRequest_whenPosting_thenCreateLesson() throws Exception {
        EduDto dto = new EduDto("NewLesson", "월수금", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(15000), false);

        mvc.perform(post("/api/workers/main/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Role", "MASTER")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 추가 - WORKER 가격 입력 시 거부")
    void givenWorkerWithTuition_whenPosting_thenForbidden() throws Exception {
        EduDto dto = new EduDto("WorkerLesson", "화목", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(30000), false);

        // 모킹된 서비스가 SecurityException을 던지도록 설정
        doThrow(new SecurityException("승인되지 않은 WORKER는 가격을 입력할 수 없습니다."))
                .when(workerMainService).createLesson(eq("WORKER"), eq("false"), any(EduDto.class));

        mvc.perform(post("/api/workers/main/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Role", "WORKER")
                        .header("Approved", "false")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("수업 추가 - WORKER 승인된 경우 허용")
    void givenApprovedWorker_whenPosting_thenAllowed() throws Exception {
        EduDto dto = new EduDto("ApprovedLesson", "화목", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(30000), true);

        mvc.perform(post("/api/workers/main/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Role", "WORKER")
                        .header("Approved", "true")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 수정 - MASTER 허용")
    void givenMasterUpdateRequest_whenPut_thenUpdateLesson() throws Exception {
        EduDto dto = new EduDto("UpdatedLesson", "매일", LocalDateTime.now(), LocalDateTime.now().plusHours(1), BigDecimal.valueOf(18000), false);

        mvc.perform(put("/api/workers/main/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Role", "MASTER")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 삭제 - MASTER 허용")
    void givenLessonId_whenDeletingAsMaster_thenDeleteLesson() throws Exception {
        mvc.perform(delete("/api/workers/main/lesson/1")
                        .header("Role", "MASTER"))
                .andExpect(status().isOk());
    }
}
