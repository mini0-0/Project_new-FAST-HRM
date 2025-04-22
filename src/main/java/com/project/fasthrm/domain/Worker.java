package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


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

    @Column(name = "worker_salary")
    private Integer workerSalary;

}