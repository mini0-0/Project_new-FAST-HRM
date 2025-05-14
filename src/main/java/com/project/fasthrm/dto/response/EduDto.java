package com.project.fasthrm.dto.response;

import com.project.fasthrm.domain.Edu;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EduDto {

    private Long eduId;
    private String eduName;
    private String eduDay;
    private LocalDateTime eduStart;
    private LocalDateTime eduEnd;
    private BigDecimal eduTuition;
    private boolean approvedForTuitionUpdate;

    @QueryProjection
    public EduDto(String eduName, String eduDay, LocalDateTime eduStart, LocalDateTime eduEnd, BigDecimal eduTuition, boolean approvedForTuitionUpdate) {
        this.eduName = eduName;
        this.eduDay = eduDay;
        this.eduStart = eduStart;
        this.eduEnd = eduEnd;
        this.eduTuition = eduTuition;
        this.approvedForTuitionUpdate = approvedForTuitionUpdate;
    }

    public Edu toEntity() {
        return Edu.builder()
                .eduName(this.eduName)
                .eduDay(this.eduDay)
                .eduStart(this.eduStart)
                .eduEnd(this.eduEnd)
                .eduTuition(this.eduTuition)
                .build();
    }

    public static EduDto from(Edu edu) {
        return EduDto.builder()
                .eduId(edu.getId())
                .eduName(edu.getEduName())
                .eduDay(edu.getEduDay())
                .eduStart(edu.getEduStart())
                .eduEnd(edu.getEduEnd())
                .eduTuition(edu.getEduTuition())
                .approvedForTuitionUpdate(
                edu.getWorker() != null && Boolean.TRUE.equals(edu.getWorker().isApprovedForTuitionUpdate())
        )
                .build();
    }


}
