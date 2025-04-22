package com.project.fasthrm;

import com.project.fasthrm.config.QuerydslConfig;
import com.project.fasthrm.domain.QEdu;
import com.project.fasthrm.domain.QMember;
import com.project.fasthrm.domain.QTakes;
import com.project.fasthrm.domain.QUser;
import com.project.fasthrm.domain.type.UserRole;
import com.project.fasthrm.dto.response.EduRevenueDto;
import com.project.fasthrm.service.MasterMainService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QuerydslConfig.class)
@MockitoBean(types = {MasterMainService.class})
public class MasterMainQueryTest {

    @Autowired private EntityManager entityManager;
    @Autowired private JPAQueryFactory queryFactory;

    private static final int REPEAT_COUNT = 500;
    private static final Long PLACE_ID = 1L;

    @Test
    void compareQueryPerformance() {
        Runtime runtime = Runtime.getRuntime();

        List<EduRevenueDto> nativeResults = runOriginalNativeSql();
        List<EduRevenueDto> improvedResults = runImprovedNativeSql();
        List<EduRevenueDto> jpqlResults = runJPQL();
        List<EduRevenueDto> querydslResults = runQueryDSL();

        Comparator<EduRevenueDto> comparator = Comparator
                .comparing(EduRevenueDto::getEduId)
                .thenComparing(EduRevenueDto::getEduName);

        nativeResults.sort(comparator);
        improvedResults.sort(comparator);
        jpqlResults.sort(comparator);
        querydslResults.sort(comparator);

        assertEquals(nativeResults, improvedResults, "개선된 Native SQL 결과가 다릅니다.");
        assertEquals(nativeResults, jpqlResults, "JPQL 결과가 다릅니다.");
        assertEquals(nativeResults, querydslResults, "QueryDSL 결과가 다릅니다.");

        // 성능 측정 (선택적)
        long nativeStart = System.currentTimeMillis();
        for (int i = 0; i < REPEAT_COUNT; i++) runOriginalNativeSql();
        long nativeEnd = System.currentTimeMillis();

        long improvedStart = System.currentTimeMillis();
        for (int i = 0; i < REPEAT_COUNT; i++) runImprovedNativeSql();
        long improvedEnd = System.currentTimeMillis();

        long jpqlStart = System.currentTimeMillis();
        for (int i = 0; i < REPEAT_COUNT; i++) runJPQL();
        long jpqlEnd = System.currentTimeMillis();

        long querydslStart = System.currentTimeMillis();
        for (int i = 0; i < REPEAT_COUNT; i++) runQueryDSL();
        long querydslEnd = System.currentTimeMillis();

        System.out.println("\n=== 성능 측정 결과 ===");
        System.out.println("1. 기존 Native SQL     : " + ((nativeEnd - nativeStart) / REPEAT_COUNT) + "ms");
        System.out.println("2. 개선된 Native SQL   : " + ((improvedEnd - improvedStart) / REPEAT_COUNT) + "ms");
        System.out.println("3. JPQL                : " + ((jpqlEnd - jpqlStart) / REPEAT_COUNT) + "ms");
        System.out.println("4. QueryDSL            : " + ((querydslEnd - querydslStart) / REPEAT_COUNT) + "ms");
    }

    private List<EduRevenueDto> runOriginalNativeSql() {
        String sql = """
            SELECT e.edu_id, e.edu_name, e.edu_tuition, COUNT(t.takes_id)
            FROM takes t
            JOIN edu e ON t.edu_id = e.edu_id
            JOIN member m ON t.member_id = m.member_id
            JOIN users u ON m.user_id = u.user_id
            WHERE u.place_id = :placeId AND u.user_role = 'MEMBER'
            GROUP BY e.edu_id, e.edu_name, e.edu_tuition
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("placeId", PLACE_ID)
                .getResultList();

        List<EduRevenueDto> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Long eduId = ((Number) row[0]).longValue();
            String eduName = (String) row[1];
            Long unitPrice = ((Number) row[2]).longValue();
            Long studentCount = ((Number) row[3]).longValue();
            dtoList.add(new EduRevenueDto(eduId, eduName, unitPrice, studentCount));
        }

        return dtoList;
    }

    private List<EduRevenueDto> runImprovedNativeSql() {
        String sql = """
            SELECT e.edu_id, e.edu_name, e.edu_tuition, COUNT(*)
            FROM takes t
            JOIN edu e ON t.edu_id = e.edu_id
            WHERE EXISTS (
                SELECT 1 FROM member m
                JOIN users u ON m.user_id = u.user_id
                WHERE m.member_id = t.member_id
                  AND u.place_id = :placeId
                  AND u.user_role = 'MEMBER'
            )
            GROUP BY e.edu_id, e.edu_name, e.edu_tuition
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("placeId", PLACE_ID)
                .getResultList();

        List<EduRevenueDto> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Long eduId = ((Number) row[0]).longValue();
            String eduName = (String) row[1];
            Long unitPrice = ((Number) row[2]).longValue();
            Long studentCount = ((Number) row[3]).longValue();
            dtoList.add(new EduRevenueDto(eduId, eduName, unitPrice, studentCount));
        }

        return dtoList;
    }

    private List<EduRevenueDto> runJPQL() {
        String jpql = """
            SELECT new com.project.fasthrm.dto.response.EduRevenueDto(
                e.id, e.eduName, e.eduTuition, COUNT(t)
            )
            FROM Takes t
            JOIN t.edu e
            JOIN t.member m
            JOIN m.user u
            WHERE u.place.id = :placeId AND u.userRole = com.project.fasthrm.domain.type.UserRole.MEMBER
            GROUP BY e.id, e.eduName, e.eduTuition
        """;

        return entityManager.createQuery(jpql, EduRevenueDto.class)
                .setParameter("placeId", PLACE_ID)
                .getResultList();
    }

    private List<EduRevenueDto> runQueryDSL() {
        QTakes t = QTakes.takes;
        QEdu e = QEdu.edu;
        QMember m = QMember.member;
        QUser u = QUser.user;

        return queryFactory.select(Projections.constructor(
                        EduRevenueDto.class,
                        e.id,
                        e.eduName,
                        e.eduTuition,
                        t.count()
                ))
                .from(t)
                .join(t.edu, e)
                .join(t.member, m)
                .join(m.user, u)
                .where(
                        u.place.id.eq(PLACE_ID)
                                .and(u.userRole.eq(UserRole.MEMBER))
                )
                .groupBy(e.id, e.eduName, e.eduTuition)
                .fetch();
    }


    @Test
    void compareJoinDepthInQueryDSL() {
        QTakes t = QTakes.takes;
        QMember m = QMember.member;
        QUser u = QUser.user;
        QEdu e = QEdu.edu;

        // 1. 3단계 JOIN 방식
        long start1 = System.nanoTime();
        List<EduRevenueDto> threeJoin = queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        e.id,
                        e.eduName,
                        e.eduTuition,
                        t.id.count()
                ))
                .from(t)
                .join(t.member, m)
                .join(m.user, u)
                .join(t.edu, e)
                .where(
                        u.place.id.eq(PLACE_ID),
                        u.userRole.eq(UserRole.MEMBER)
                )
                .groupBy(e.id, e.eduName, e.eduTuition)
                .fetch();
        long end1 = System.nanoTime();

        // 2. 단축 JOIN (중첩 join 한 줄 처리)
        long start2 = System.nanoTime();
        List<EduRevenueDto> nestedJoin = queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        e.id,
                        e.eduName,
                        e.eduTuition,
                        t.id.count()
                ))
                .from(t)
                .join(t.member.user, u)
                .join(t.edu, e)
                .where(
                        u.place.id.eq(PLACE_ID),
                        u.userRole.eq(UserRole.MEMBER)
                )
                .groupBy(e.id, e.eduName, e.eduTuition)
                .fetch();
        long end2 = System.nanoTime();

        // 3. 결과 비교
        Comparator<EduRevenueDto> comparator = Comparator
                .comparing(EduRevenueDto::getEduId)
                .thenComparing(EduRevenueDto::getEduName);

        threeJoin.sort(comparator);
        nestedJoin.sort(comparator);

        assertThat(threeJoin).usingRecursiveComparison().isEqualTo(nestedJoin);

        // 4. 성능 출력
        System.out.printf("3단계 JOIN: %.3f ms\n", (end1 - start1) / 1_000_000.0);
        System.out.printf("중첩 JOIN: %.3f ms\n", (end2 - start2) / 1_000_000.0);
    }

}
