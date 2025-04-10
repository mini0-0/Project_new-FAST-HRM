package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "test", indexes = {
        @Index(name = "idx_test_day", columnList = "test_day")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Test extends BaseTime {

    // 시험 결과
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="test_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "takes_id", nullable = false)
    private Takes takes;

    @Column(name = "test_day")
    private LocalDate testDay;

    @Column(name = "test_result")
    private String testResult;

    @Column(name = "test_name")
    private String testName;

}