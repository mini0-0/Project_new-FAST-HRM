package com.project.fasthrm.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class EduRevenueDto {
    // 교육별 수익
    private Long eduId;
    private String eduName;       // 교육명
    private Long studentCount;    // 수강생 수
    private Long unitPrice;       // 1인당 수강료
    private Long revenue;         // 총 수익 (계산값)


    @QueryProjection
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EduRevenueDto that = (EduRevenueDto) o;
        return Objects.equals(eduId, that.eduId) && Objects.equals(eduName, that.eduName) && Objects.equals(studentCount, that.studentCount) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(revenue, that.revenue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eduId, eduName, studentCount, unitPrice, revenue);
    }

    @Override
    public String toString() {
        return "EduRevenueDto{" +
                "eduId=" + eduId +
                ", eduName='" + eduName + '\'' +
                ", studentCount=" + studentCount +
                ", unitPrice=" + unitPrice +
                ", revenue=" + revenue +
                '}';
    }
}