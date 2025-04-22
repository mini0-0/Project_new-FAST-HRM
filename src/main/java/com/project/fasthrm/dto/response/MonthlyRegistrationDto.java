package com.project.fasthrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyRegistrationDto {
    // 월별 등록자 수
    private String month;             // 월
    private Long totalRegistrations; // 등록자 수
}
