package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
public class MonthlyRegistrationDto {
    // 월별 등록자 수
    private String month;             // 월
    private Long totalRegistrations; // 등록자 수

    @QueryProjection
    public MonthlyRegistrationDto(String month, Long totalRegistrations) {
        this.month = month;
        this.totalRegistrations = totalRegistrations;
    }
}
