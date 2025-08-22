package com.project.fasthrm.repository.query;
import com.project.fasthrm.domain.*;
import com.project.fasthrm.domain.type.AttendanceStatus;
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

    private final QWorker worker = new QWorker("worker");
    private final QUser user = new QUser("user");
    private final QWorkTime workTime = new QWorkTime("workTime");


    @Override
    public List<WorkerManageDto> findWorkerByPlaceId(Long placeId) {

        return queryFactory
                .select(Projections.fields(
                        WorkerManageDto.class,
                        user.id.as("userId"),
                        user.place.id.as("placeId"),
                        user.username.as("username"),
                        user.password.as("password"),
                        user.userRealName.as("userRealName"),
                        user.userAddress.as("userAddress"),
                        user.userPhoneNumber.as("userPhoneNumber"),
                        worker.workerSalary.as("workerSalary"),
                        workTime.time.as("scheduledWorkTime"),
                        workTime.start.as("workStartTime"),
                        workTime.end.as("workEndTime"),
                        workTime.type.as("todayAttendance")
                ))
                .from(user)
                .leftJoin(worker).on(user.id.eq(worker.user.id))
                .leftJoin(workTime).on(worker.id.eq(workTime.worker.id))
                .where(user.place.id.eq(placeId))
                .fetch();
    }


    @Override
    public void updateWorkerManage(WorkerManageDto dto) {

        queryFactory.update(worker)
                .where(worker.user.id.eq(dto.getUserId()))
                .set(worker.workerSalary, dto.getWorkerSalary())
                .execute();

        queryFactory.update(workTime)
                .where(workTime.worker.user.id.eq(dto.getUserId()))
                .set(workTime.type, dto.getTodayAttendance())
                .set(workTime.time, dto.getScheduledWorkTime())
                .set(workTime.start, dto.getWorkStartTime())
                .set(workTime.end, dto.getWorkEndTime())
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
