package com.project.fasthrm.api;

import com.project.fasthrm.dto.response.WorkerMainDto;
import com.project.fasthrm.service.WorkerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API: WorkerMain")
@WebMvcTest(WorkerMainApi.class)
public class WorkerMainApiTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private WorkerService workerService;


    @Test
    @DisplayName("WorkerMain - placeId에 따라 worker 리스트 정상 반환 ")
    void givenPlaceId_whenRequestingWorkerMainApi_thenReturnsWorkerView() throws Exception {
        // Given
        List<WorkerMainDto> mockWorkers = List.of(
                new WorkerMainDto(1L, "worker1","password", "Worker One", "서울시", "01012345678", 3000000),
                new WorkerMainDto(2L, "worker2","password", "Worker Two", "경기도", "01078941234", 4000000)
        );
        Mockito.when(workerService.findWorkerByPlaceId(anyLong())).thenReturn(mockWorkers);

        // When & Then
        mvc.perform(get("/api/worker/main").param("placeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // 리스트 크기 2개 확인
                .andExpect(jsonPath("$[0].username").value("worker1"))
                .andExpect(jsonPath("$[0].password").value("password"))
                .andExpect(jsonPath("$[0].userRealName").value("Worker One"))
                .andExpect(jsonPath("$[0].userAddress").value("서울시"))
                .andExpect(jsonPath("$[0].userPhoneNumber").value("01012345678"))
                .andExpect(jsonPath("$[0].workerSalary").value(3000000))

                .andExpect(jsonPath("$[1].username").value("worker2"))
                .andExpect(jsonPath("$[1].password").value("password"))
                .andExpect(jsonPath("$[1].userRealName").value("Worker Two"))
                .andExpect(jsonPath("$[1].userAddress").value("경기도"))
                .andExpect(jsonPath("$[1].userPhoneNumber").value("01078941234"))
                .andExpect(jsonPath("$[1].workerSalary").value(4000000));




    }
}
