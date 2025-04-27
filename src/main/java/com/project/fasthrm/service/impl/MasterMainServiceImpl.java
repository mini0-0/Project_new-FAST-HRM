package com.project.fasthrm.service.impl;

import com.project.fasthrm.dto.response.MasterMainDto;

import com.project.fasthrm.repository.query.MasterMainQueryRepository;
import com.project.fasthrm.service.MasterMainService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MasterMainServiceImpl implements MasterMainService {

    private final MasterMainQueryRepository repository;

    @Override
    public MasterMainDto getMasterMainDashboard(Long placeId) {
        return MasterMainDto.builder()
                .eduRevenueList(repository.getEduRevenue(placeId))
                .monthlyEduRevenueList(repository.getMonthlyEduRevenue(placeId))
                .monthlyRegistrationList(repository.getMonthlyRegistration(placeId))
                .build();
    }


}
