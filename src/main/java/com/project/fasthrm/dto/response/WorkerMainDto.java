package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkerMainDto {

    private Long userId;
    private String username;
    private String password;
    private String userRealName;
    private String userAddress;
    private String userPhoneNumber;
    private Integer workerSalary;

    @QueryProjection
    public WorkerMainDto(Long userId, String username, String password, String userRealName,
                         String userAddress, String userPhoneNumber, Integer workerSalary) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userRealName = userRealName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.workerSalary = workerSalary;
    }
}
