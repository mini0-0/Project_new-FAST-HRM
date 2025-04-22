package com.project.fasthrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyEduRevenueDto {
    // 월별 수익
    private String month;
    private Long totalRevenue;   // 월별 총 수익

}