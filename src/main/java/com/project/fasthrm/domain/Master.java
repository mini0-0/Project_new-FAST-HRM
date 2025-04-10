package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Master extends BaseTime {

    // 관리자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="master_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

}