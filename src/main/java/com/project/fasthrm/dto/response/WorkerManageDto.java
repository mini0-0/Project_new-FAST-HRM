package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WorkerManageDto {

    private Long userId;
    private Long placeId;
    private String username;
    private String password;
    private String userRealName;
    private String userAddress;
    private String userPhoneNumber;
    private Integer workerSalary;

    private String scheduledWorkTime; // 약속된 업무 시간
    private LocalDateTime workStartTime; // 출근 시간
    private LocalDateTime workEndTime;   // 퇴근 시간

    private Boolean todayAttendance; // 오늘 출근 여부
    private Boolean isLate;          // 지각 여부
    private Boolean usedVacation;    // 휴가 사용 여부
    private Integer monthlyWorkHours; // 이번 달 누적 근무 시간
    private Integer weeklyWorkHours; // 이번 주 누적 근무 시간


    @Builder
    @QueryProjection
    public WorkerManageDto(Long userId, Long placeId, String username, String password, String userRealName,
                           String userAddress, String userPhoneNumber, Integer workerSalary,
                           String scheduledWorkTime, LocalDateTime workStartTime, LocalDateTime workEndTime,
                           Boolean todayAttendance, Boolean isLate, Boolean usedVacation,
                           Integer monthlyWorkHours, Integer weeklyWorkHours) {
        this.userId = userId;
        this.placeId = placeId;
        this.username = username;
        this.password = password;
        this.userRealName = userRealName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.workerSalary = workerSalary;
        this.scheduledWorkTime = scheduledWorkTime;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.todayAttendance = todayAttendance;
        this.isLate = isLate;
        this.usedVacation = usedVacation;
        this.monthlyWorkHours = monthlyWorkHours;
        this.weeklyWorkHours = weeklyWorkHours;

    }

    public void setTodayAttendance(Boolean todayAttendance) {
        this.todayAttendance = todayAttendance;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public void setUsedVacation(Boolean usedVacation) {
        this.usedVacation = usedVacation;
    }

    public void setMonthlyWorkHours(Integer monthlyWorkHours) {
        this.monthlyWorkHours = monthlyWorkHours;
    }

    public void setWeeklyWorkHours(Integer weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

}
