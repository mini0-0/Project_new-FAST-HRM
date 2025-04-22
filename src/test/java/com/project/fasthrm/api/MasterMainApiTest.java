package com.project.fasthrm.api;


import com.project.fasthrm.dto.response.EduRevenueDto;
import com.project.fasthrm.dto.response.MasterMainDto;
import com.project.fasthrm.dto.response.MonthlyEduRevenueDto;
import com.project.fasthrm.dto.response.MonthlyRegistrationDto;
import com.project.fasthrm.service.MasterMainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = MasterMainApi.class)
@DisplayName("API: MasterMain")
class MasterMainApiTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private MasterMainService masterMainService;

    @Test
    @DisplayName("장소 ID가 주어졌을 때 대시보드 데이터 반환")
    void shouldReturnDashboardData() throws Exception {
        // Given
        Long placeId = 1L;
        MasterMainDto mockDto = MasterMainDto.builder()
                .eduRevenueList(List.of(
                        EduRevenueDto.builder()
                                .eduName("Java 기초")
                                .studentCount(10L)
                                .unitPrice(50000L)
                                .revenue(500000L)
                                .build()
                ))
                .monthlyEduRevenueList(List.of(
                        MonthlyEduRevenueDto.builder()
                                .month("1월")
                                .totalRevenue(500000L)
                                .build()
                ))
                .monthlyRegistrationList(List.of(
                        MonthlyRegistrationDto.builder()
                                .month("1월")
                                .totalRegistrations(10L)
                                .build()
                ))
                .build();

        given(masterMainService.getMasterMainDashboard(placeId)).willReturn(mockDto);

        // When & Then
        mvc.perform(get("/api/masters/main").param("placeId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eduRevenueList[0].eduName").value("Java 기초"))
                .andExpect(jsonPath("$.monthlyEduRevenueList[0].month").value("1월"))
                .andExpect(jsonPath("$.monthlyRegistrationList[0].month").value("1월"));
    }
}
