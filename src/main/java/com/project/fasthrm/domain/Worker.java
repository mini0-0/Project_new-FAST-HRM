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

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkTime> workTimes = new ArrayList<>();

    @Column(name = "worker_salary")
    private Integer workerSalary;

    @Column(name = "approved_for_tuition_update", nullable = false)
    private boolean approvedForTuitionUpdate = false;

}