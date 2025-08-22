package com.project.fasthrm.dto.response;

import com.project.fasthrm.domain.type.AttendanceStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    private AttendanceStatus todayAttendance; // 오늘 출근/지각/결석 여부
    private Boolean usedVacation;    // 휴가 사용 여부
    private Integer monthlyWorkHours; // 이번 달 누적 근무 시간
    private Integer weeklyWorkHours; // 이번 주 누적 근무 시간


    @Builder
    @QueryProjection
    public WorkerManageDto(Long userId, Long placeId, String username, String password, String userRealName,
                           String userAddress, String userPhoneNumber, Integer workerSalary,
                           String scheduledWorkTime, LocalDateTime workStartTime, LocalDateTime workEndTime,
                           AttendanceStatus todayAttendance, Boolean usedVacation,
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
        this.usedVacation = usedVacation;
        this.monthlyWorkHours = monthlyWorkHours;
        this.weeklyWorkHours = weeklyWorkHours;

    }

}
