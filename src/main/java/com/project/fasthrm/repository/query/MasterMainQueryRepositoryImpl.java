package com.project.fasthrm.repository.query;

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

    private final QEdu edu = new QEdu("edu");
    private final QMember member = new QMember("member");
    private final QTakes takes = new QTakes("takes");
    private final QUser user = new QUser("user");


    // 교육별 총 누적수익
    @Override
    public List<EduRevenueDto> getEduRevenue(Long placeId) {

        return queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        edu.id,
                        edu.eduName,
                        edu.eduTuition,
                        takes.count()
                ))
                .from(takes)
                .join(takes.member.user, user)
                .join(takes.edu, edu)
                .where(
                        user.place.id.eq(placeId),
                        user.userRole.eq(UserRole.MEMBER)
                )
                .groupBy(edu.id, edu.eduName, edu.eduTuition)
                .fetch();


    }

    // 월별 총수익
    @Override
    public List<MonthlyEduRevenueDto> getMonthlyEduRevenue(Long placeId) {

        return queryFactory
                .select(Projections.constructor(
                        MonthlyEduRevenueDto.class,
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", takes.registeredAt),
                        Expressions.stringTemplate("SUM({0})", edu.eduTuition)
                                .castToNum(Long.class)
                ))
                .from(takes)
                .join(takes.edu, edu)
                .join(takes.member.user, user)
                .where(user.place.id.eq(placeId), user.userRole.eq(UserRole.MEMBER))
                .groupBy(Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", takes.registeredAt))
                .fetch();
    }

    // 월별 등록자 수
    @Override
    public List<MonthlyRegistrationDto> getMonthlyRegistration(Long placeId) {

        return queryFactory
                .select(Projections.constructor(
                        MonthlyRegistrationDto.class,
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", takes.registeredAt),
                        takes.count()
                ))
                .from(takes)
                .join(takes.member.user, user)
                .where(user.place.id.eq(placeId), user.userRole.eq(UserRole.MEMBER))
                .groupBy(Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", takes.registeredAt))
                .fetch();

    }

}
