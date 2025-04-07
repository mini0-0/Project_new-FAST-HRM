package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hc", indexes = {
        @Index(name = "idx_member_date", columnList = "member_id, hc_date")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class HealthCheck extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="hc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "hc_sex", length = 10)
    private String hcSex;

    @Column(name = "hc_height")
    private Float hcHeight;

    @Column(name = "hc_weight")
    private Float hcWeight;

    @Column(name = "hc_date", nullable = false)
    private LocalDateTime hcDate;

    @Column(name = "hc_purpose")
    private String hcPurpose;

    @Column(name = "hc_totalbodywater", precision = 5, scale = 2)
    private BigDecimal hcTotalbodywater;

    @Column(name = "hc_protein", precision = 5, scale = 2)
    private BigDecimal hcProtein;

    @Column(name = "hc_minerals", precision = 5, scale = 2)
    private BigDecimal hcMinerals;

    @Column(name = "hc_bodyfatmass", precision = 5, scale = 2)
    private BigDecimal hcBodyfatmass;

    @Column(name = "hc_skeletalmusclemass", precision = 5, scale = 2)
    private BigDecimal hcSkeletalmusclemass;

    @Column(name = "hc_report", columnDefinition = "TEXT")
    private String hcReport;

}