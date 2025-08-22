package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "takes", indexes = {
        @Index(name = "idx_registered_at", columnList = "registered_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Takes extends BaseTime {

    // edu를 듣는 회원을 확인하기 위해서 사용
    // 회원 일정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="takes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edu_id", nullable = false)
    private Edu edu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "registered_at", nullable = false)
    private LocalDate registeredAt;

}