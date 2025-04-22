package com.project.fasthrm.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MasterMainDto {

    private List<EduRevenueDto> eduRevenueList; // 교육별 수익
    private List<MonthlyEduRevenueDto> monthlyEduRevenueList; // 월별 수익
    private List<MonthlyRegistrationDto> monthlyRegistrationList; // 월별 등록자 수

}
