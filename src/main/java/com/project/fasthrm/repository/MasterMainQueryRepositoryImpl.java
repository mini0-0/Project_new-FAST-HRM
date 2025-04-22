package com.project.fasthrm.repository;

import com.project.fasthrm.domain.QEdu;
import com.project.fasthrm.domain.QMember;
import com.project.fasthrm.domain.QTakes;
import com.project.fasthrm.domain.QUser;
import com.project.fasthrm.domain.type.UserRole;
import com.project.fasthrm.dto.response.EduRevenueDto;
import com.project.fasthrm.dto.response.MonthlyEduRevenueDto;
import com.project.fasthrm.dto.response.MonthlyRegistrationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MasterMainQueryRepositoryImpl implements MasterMainQueryRepository{

    private final JPAQueryFactory queryFactory;

    // 교육별 총 누적수익
    @Override
    public List<EduRevenueDto> getEduRevenue(Long placeId) {
        QTakes t = QTakes.takes;
        QEdu e = QEdu.edu;
        QMember m = QMember.member;
        QUser u = QUser.user;


        return queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        e.id,
                        e.eduName,
                        e.eduTuition,
                        t.count()
                ))
                .from(t)
                .join(t.member.user, u)
                .join(t.edu, e)
                .where(
                        u.place.id.eq(placeId),
                        u.userRole.eq(UserRole.MEMBER)
                )
                .groupBy(e.id, e.eduName, e.eduTuition)
                .fetch();


    }

    // 월별 총수익
    @Override
    public List<MonthlyEduRevenueDto> getMonthlyEduRevenue(Long placeId) {
        QTakes t = QTakes.takes;
        QEdu e = QEdu.edu;
        QUser u = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        MonthlyEduRevenueDto.class,
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", t.registeredAt),
                        Expressions.stringTemplate("SUM({0})", e.eduTuition)
                                .castToNum(Long.class)
                ))
                .from(t)
                .join(t.edu, e)
                .join(t.member.user, u)
                .where(u.place.id.eq(placeId), u.userRole.eq(UserRole.MEMBER))
                .groupBy(Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", t.registeredAt))
                .fetch();
    }

    // 월별 등록자 수
    @Override
    public List<MonthlyRegistrationDto> getMonthlyRegistration(Long placeId) {
        QTakes t = QTakes.takes;
        QUser u = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        MonthlyRegistrationDto.class,
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", t.registeredAt),
                        t.count()
                ))
                .from(t)
                .join(t.member.user, u)
                .where(u.place.id.eq(placeId), u.userRole.eq(UserRole.MEMBER))
                .groupBy(Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", t.registeredAt))
                .fetch();

    }

}
