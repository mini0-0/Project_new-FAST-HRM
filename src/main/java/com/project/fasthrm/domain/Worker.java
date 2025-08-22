package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "worker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Worker extends BaseTime {

    // 직원
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="worker_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkTime> workTimes = new ArrayList<>();

    // 직원 월급
    @Column(name = "worker_salary")
    private Integer workerSalary;

    // 수업료 수정 승인 여부
    @Builder.Default
    @Column(name = "approved_for_tuition_update", nullable = false)
    private boolean approvedForTuitionUpdate = false;

}