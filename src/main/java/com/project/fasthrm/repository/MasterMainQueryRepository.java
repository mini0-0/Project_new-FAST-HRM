package com.project.fasthrm.repository;

import com.project.fasthrm.dto.response.EduRevenueDto;
import com.project.fasthrm.dto.response.MonthlyEduRevenueDto;
import com.project.fasthrm.dto.response.MonthlyRegistrationDto;

import java.util.List;

public interface MasterMainQueryRepository {
    List<EduRevenueDto> getEduRevenue(Long placeId);
    List<MonthlyEduRevenueDto> getMonthlyEduRevenue(Long placeId);
    List<MonthlyRegistrationDto> getMonthlyRegistration(Long placeId);

}
