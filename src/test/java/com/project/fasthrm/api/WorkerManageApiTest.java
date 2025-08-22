package com.project.fasthrm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fasthrm.domain.type.AttendanceStatus;
import com.project.fasthrm.dto.response.WorkerManageDto;
import com.project.fasthrm.service.WorkerManageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API: WorkerMain(placeId에 따라)")
@WebMvcTest(WorkerManageApi.class)
public class WorkerManageApiTest {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private WorkerManageService workerManageService;


    @Test
    @DisplayName("WorkerManage - worker 리스트 정상 반환 ")
    void givenPlaceId_whenRequestingWorkerMainApi_thenReturnsWorkerView() throws Exception {
        // Given
        List<WorkerManageDto> mockWorkers = List.of(
                createWorkerManageDto1(),
                createWorkerManageDto2()
        );

        Mockito.when(workerManageService.findWorkerByPlaceId(1L)).thenReturn(mockWorkers);

        // When & Then
        mvc.perform(get("/api/workers/manage")
                        .param("placeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("worker1"))
                .andExpect(jsonPath("$[1].username").value("worker2"));
    }

    @Test
    @DisplayName("WorkerManage - worker 정보 수정")
    void givenWorkerManageDto_whenUpdatingWorker_thenReturnsOk() throws Exception {
        // Given
        WorkerManageDto dto = WorkerManageDto.builder()
                .userId(1L)
                .placeId(1L)
                .username("worker1")
                .userRealName("Worker One")
                .build();

        // When & Then
        mvc.perform(put("/api/workers/manage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("WorkerManage - worker 정보 삭제")
    void givenUserId_whenDeletingWorker_thenReturnsOk() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mvc.perform(delete("/api/workers/manage")
                .param("userId", userId.toString()))
                .andExpect(status().isOk());


    }

    private WorkerManageDto createWorkerManageDto1() {
        return WorkerManageDto.builder()
                .userId(1L)
                .placeId(1L)
                .username("worker1")
                .userRealName("Worker One")
                .workerSalary(3000000)
                .scheduledWorkTime("09:00-18:00")
                .workStartTime(LocalDateTime.parse("2025-08-21T09:00:00"))
                .workEndTime(LocalDateTime.parse("2025-08-21T18:00:00"))
                .todayAttendance(AttendanceStatus.PRESENT)
                .usedVacation(false)
                .build();
    }

    private WorkerManageDto createWorkerManageDto1Updated() {
        return WorkerManageDto.builder()
                .userId(2L)
                .placeId(1L)
                .username("worker2")
                .userRealName("Worker Two")
                .workerSalary(3100000)
                .scheduledWorkTime("10:00-19:00")
                .workStartTime(LocalDateTime.parse("2025-08-21T10:05:00"))
                .workEndTime(LocalDateTime.parse("2025-08-21T19:00:00"))
                .todayAttendance(AttendanceStatus.LATE)
                .usedVacation(false)
                .build();
    }

    private WorkerManageDto createWorkerManageDto2() {
        return WorkerManageDto.builder()
                .userId(2L)
                .placeId(2L)
                .username("worker2")
                .password("password")
                .userRealName("Worker Two")
                .userAddress("경기도")
                .userPhoneNumber("01078941234")
                .workerSalary(4000000)
                .scheduledWorkTime("10:00-19:00")
                .workStartTime(LocalDateTime.parse("2025-08-21T10:00:00"))
                .workEndTime(LocalDateTime.parse("2025-08-21T19:00:00"))
                .todayAttendance(AttendanceStatus.PRESENT)
                .usedVacation(false)
                .monthlyWorkHours(140)
                .weeklyWorkHours(35)
                .build();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
