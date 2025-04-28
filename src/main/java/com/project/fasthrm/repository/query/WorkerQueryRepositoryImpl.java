package com.project.fasthrm.repository.query;
import com.project.fasthrm.domain.*;
import com.project.fasthrm.dto.response.WorkerManageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.types.ExpressionUtils;
import org.springframework.transaction.annotation.Transactional;


@Repository
@RequiredArgsConstructor
public class WorkerQueryRepositoryImpl implements WorkerQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public List<WorkerManageDto> findWorkerByPlaceId(Long placeId) {
        QUser user = QUser.user;
        QWorker worker = QWorker.worker;
        QWorkTime workTime = QWorkTime.workTime;

        return queryFactory
                .select(Projections.fields(
                        WorkerManageDto.class,
                        user.id.as("userId"),
                        user.place.id.as("placeId"),
                        user.username,
                        user.password,
                        user.userRealName,
                        user.userAddress,
                        user.userPhoneNumber,
                        worker.workerSalary,
                        workTime.time.as("scheduledWorkTime"),
                        workTime.start.as("workStartTime"),
                        workTime.end.as("workEndTime"),
                        ExpressionUtils.as(workTime.start.isNotNull(), "todayAttendance"),
                        ExpressionUtils.as(
                                workTime.start.gt(
                                        Expressions.dateTimeTemplate(LocalDateTime.class,
                                                "cast(CONCAT({0}, ':00') as datetime)", workTime.time)
                                ), "isLate"),
                        ExpressionUtils.as(Expressions.constant(false), "usedVacation"),
                        ExpressionUtils.as(Expressions.constant(0), "monthlyWorkHours"),
                        ExpressionUtils.as(Expressions.constant(0), "weeklyWorkHours")
                ))
                .from(user)
                .leftJoin(worker).on(user.id.eq(worker.user.id))
                .leftJoin(workTime).on(worker.id.eq(workTime.worker.id))
                .where(user.place.id.eq(placeId))
                .fetch();
    }


    @Override
    public void updateWorkerManage(WorkerManageDto dto) {
        QWorker worker = QWorker.worker;
        QWorkTime workTime = QWorkTime.workTime;

        queryFactory.update(worker)
                .where(worker.user.id.eq(dto.getUserId()))
                .set(worker.workerSalary, dto.getWorkerSalary())
                .execute();

        queryFactory.update(workTime)
                .where(workTime.worker.user.id.eq(dto.getUserId()))
                .set(workTime.time, dto.getScheduledWorkTime())
                .set(workTime.type, dto.getUsedVacation() ? "휴가" : "출근")
                .execute();
    }

    @Override
    @Transactional
    public void deleteWorkerManage(Long userId) {
        User user = em.find(User.class, userId);
        if (user != null) {
            em.remove(user);
        }

    }
}
