package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "edu", indexes = {
        @Index(name = "idx_place_id", columnList = "place_id"),
        @Index(name = "idx_worker_id", columnList = "worker_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Edu extends BaseTime {

    // 개설 서비스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="edu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "edu_name", nullable = false)
    private String eduName;

    @Column(name = "edu_day", nullable = false, length = 50)
    private String eduDay;

    @Column(name = "edu_start", nullable = false)
    private LocalDateTime eduStart;

    @Column(name = "edu_end", nullable = false)
    private LocalDateTime eduEnd;

    @Column(name = "edu_tuition", precision = 10, scale = 2)
    private BigDecimal eduTuition;

}