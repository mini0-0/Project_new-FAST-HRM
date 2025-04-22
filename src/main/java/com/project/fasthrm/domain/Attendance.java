package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "attendance", indexes = {
        @Index(name = "idx_attendance_member_date", columnList = "member_id, attendance_datetime")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Attendance extends BaseTime {

    // 회원 출석 정보
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "attendance_datetime", nullable = false)
    private LocalDateTime attendanceDatetime;

}