package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
public class MonthlyEduRevenueDto {
    // 월별 수익
    private String month;
    private Long totalRevenue;   // 월별 총 수익


    @QueryProjection
    public MonthlyEduRevenueDto(String month, Long totalRevenue) {
        this.month = month;
        this.totalRevenue = totalRevenue;
    }

}