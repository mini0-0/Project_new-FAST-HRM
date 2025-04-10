package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "worktime", indexes = {
        @Index(name = "idx_worker_day", columnList = "worker_id, worktime_day")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WorkTime extends BaseTime {

    // 직원 출퇴근 정보
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worktime_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(name = "worktime_day")
    private LocalDate worktimeDay;

    @Column(name = "worktime_type")
    private String type;

    @Column(name = "worktime_time")
    private String time;

    @Column(name = "worktime_start")
    private LocalDateTime start;

    @Column(name = "worktime_end")
    private LocalDateTime end;
}
