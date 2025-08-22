package com.project.fasthrm.domain;

import com.project.fasthrm.domain.type.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "worktime_day") // 근무 날짜
    private LocalDate worktimeDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "worktime_type") // 출근/퇴근/지각/결근/휴가
    private AttendanceStatus type;

    @Column(name = "worktime_time") // 약속한 업무시간
    private String time;

    @Column(name = "worktime_start") // 출근 시간
    private LocalDateTime start;

    @Column(name = "worktime_end")  // 퇴근시간
    private LocalDateTime end;
}
