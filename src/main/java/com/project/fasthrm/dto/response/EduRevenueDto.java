package com.project.fasthrm.dto.response;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class EduRevenueDto {
    // 교육별 수익
    private Long eduId;
    private String eduName;       // 교육명
    private Long studentCount;    // 수강생 수
    private Long unitPrice;       // 1인당 수강료
    private Long revenue;         // 총 수익 (계산값)


    public EduRevenueDto(Long eduId, String eduName, Long unitPrice, Long studentCount) {
        this.eduId = eduId;
        this.eduName = eduName;
        this.unitPrice = unitPrice;
        this.studentCount = studentCount;
        this.revenue = unitPrice * studentCount;
    }

    public EduRevenueDto(Long eduId, String eduName, BigDecimal unitPrice, Long studentCount) {
        this.eduId = eduId;
        this.eduName = eduName;
        this.unitPrice = unitPrice != null ? unitPrice.longValue() : 0L;
        this.studentCount = studentCount;
        this.revenue = this.unitPrice * studentCount;
    }

}