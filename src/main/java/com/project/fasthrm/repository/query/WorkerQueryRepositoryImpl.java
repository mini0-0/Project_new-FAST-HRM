package com.project.fasthrm.repository.query;

import com.project.fasthrm.domain.QUser;
import com.project.fasthrm.domain.QWorker;
import com.project.fasthrm.dto.response.QWorkerMainDto;
import com.project.fasthrm.dto.response.WorkerMainDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkerQueryRepositoryImpl implements WorkerQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WorkerMainDto> findWorkerByPlaceId(Long placeId) {
        QWorker worker = QWorker.worker;
        QUser user = QUser.user;

        return queryFactory
                .select(new QWorkerMainDto(
                        user.id,
                        user.username,
                        user.password,
                        user.userRealName,
                        user.userAddress,
                        user.userPhoneNumber,
                        worker.workerSalary
                ))
                .from(worker)
                .join(worker.user, user)
                .where(user.place.id.eq(placeId))
                .fetch();
    }
}
