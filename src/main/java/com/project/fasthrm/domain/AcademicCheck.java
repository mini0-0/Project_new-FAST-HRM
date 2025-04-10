package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ac", indexes = {
        @Index(name = "idx_member_date", columnList = "member_id, ac_date")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AcademicCheck extends BaseTime {

    // 학원
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ac_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "ac_grade")
    private Integer acGrade;

    @Column(name = "ac_class", nullable = false, length = 50)
    private String acClass;

    @Column(name = "ac_school")
    private String acSchool;

    @Column(name = "ac_parent")
    private String acParent;

    @Column(name = "ac_date", nullable = false)
    private LocalDateTime acDate;

}