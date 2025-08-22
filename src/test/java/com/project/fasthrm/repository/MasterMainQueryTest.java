package com.project.fasthrm.repository;

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
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(QuerydslConfig.class)
@MockitoBean(types = {MasterMainService.class})
@DisplayName("MasterMainQueryTest - 수업 매출 집계 비교 및 성능 측정")
public class MasterMainQueryTest {

    @Autowired private EntityManager entityManager;
    @Autowired private JPAQueryFactory queryFactory;

    private static final Long PLACE_ID = 1L;

    private final QEdu edu = new QEdu("edu");
    private final QMember member = new QMember("member");
    private final QUser user = new QUser("user");
    private final QTakes takes = new QTakes("takes");

    @Test
    @DisplayName("집계 결과: Native / Improved Native / JPQL / QueryDSL 일치 검증")
    void compareQueryPerformance() {
        List<EduRevenueDto> nativeResults   = runOriginalNativeSql();
        List<EduRevenueDto> improvedResults = runImprovedNativeSql();
        List<EduRevenueDto> jpqlResults     = runJPQL();
        List<EduRevenueDto> querydslResults = runQueryDSL();

        Comparator<EduRevenueDto> comparator = Comparator
                .comparing(EduRevenueDto::getEduId)
                .thenComparing(EduRevenueDto::getEduName);

        nativeResults.sort(comparator);
        improvedResults.sort(comparator);
        jpqlResults.sort(comparator);
        querydslResults.sort(comparator);

        RecursiveComparisonConfiguration cfg = RecursiveComparisonConfiguration.builder()
                .withComparatorForType(
                        (BigDecimal a, BigDecimal b) -> a.compareTo(b),
                        BigDecimal.class
                )
                .build();

        assertThat(improvedResults)
                .usingRecursiveFieldByFieldElementComparator(cfg)
                .containsExactlyElementsOf(nativeResults);

        assertThat(jpqlResults)
                .usingRecursiveFieldByFieldElementComparator(cfg)
                .containsExactlyElementsOf(nativeResults);

        assertThat(querydslResults)
                .usingRecursiveFieldByFieldElementComparator(cfg)
                .containsExactlyElementsOf(nativeResults);
    }

    @Test
    @DisplayName("QueryDSL: 3단계 JOIN vs 중첩 JOIN 성능 비교 (평균 ms 출력)")
    void compareJoinDepthInQueryDSL() {
        final int WARMUP = 10;
        final int N = 200;

        // 정확성 검증
        List<EduRevenueDto> threeJoinOnce = queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        edu.id, edu.eduName, edu.eduTuition, takes.id.count()
                ))
                .from(takes)
                .join(takes.member, member)
                .join(member.user, user)
                .join(takes.edu, edu)
                .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                .groupBy(edu.id, edu.eduName, edu.eduTuition)
                .fetch();

        List<EduRevenueDto> nestedJoinOnce = queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        edu.id, edu.eduName, edu.eduTuition, takes.id.count()
                ))
                .from(takes)
                .join(takes.member.user, user)
                .join(takes.edu, edu)
                .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                .groupBy(edu.id, edu.eduName, edu.eduTuition)
                .fetch();

        Comparator<EduRevenueDto> cmp = Comparator
                .comparing(EduRevenueDto::getEduId)
                .thenComparing(EduRevenueDto::getEduName);

        threeJoinOnce.sort(cmp);
        nestedJoinOnce.sort(cmp);
        assertThat(nestedJoinOnce)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(threeJoinOnce);

        // Warmup
        for (int i = 0; i < WARMUP; i++) {
            queryFactory.select(takes.id.count())
                    .from(takes).join(takes.member, member).join(member.user, user).join(takes.edu, edu)
                    .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                    .groupBy(edu.id, edu.eduName, edu.eduTuition).fetch();
            entityManager.clear();

            queryFactory.select(takes.id.count())
                    .from(takes).join(takes.member.user, user).join(takes.edu, edu)
                    .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                    .groupBy(edu.id, edu.eduName, edu.eduTuition).fetch();
            entityManager.clear();
        }

        // 측정: 3단계 JOIN
        long start1 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            queryFactory.select(takes.id.count())
                    .from(takes).join(takes.member, member).join(member.user, user).join(takes.edu, edu)
                    .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                    .groupBy(edu.id, edu.eduName, edu.eduTuition).fetch();
            entityManager.clear();
        }
        long end1 = System.nanoTime();

        // 측정: 중첩 JOIN
        long start2 = System.nanoTime();
        for (int i = 0; i < N; i++) {
            queryFactory.select(takes.id.count())
                    .from(takes).join(takes.member.user, user).join(takes.edu, edu)
                    .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                    .groupBy(edu.id, edu.eduName, edu.eduTuition).fetch();
            entityManager.clear();
        }
        long end2 = System.nanoTime();

        double threeJoinMs = (end1 - start1) / 1_000_000.0 / N;
        double nestedJoinMs = (end2 - start2) / 1_000_000.0 / N;

        System.out.printf("3단계 JOIN: %.3f ms\n", threeJoinMs);
        System.out.printf("중첩 JOIN: %.3f ms\n", nestedJoinMs);
    }

    @Test
    @DisplayName("Native vs JPQL vs QueryDSL 성능 비교 (평균 ms 출력)")
    void compareApiStylesPerformance() {
        final int WARMUP = 10;
        final int N = 200;

        Runnable clear = () -> entityManager.clear();

        // Warmup
        for (int i = 0; i < WARMUP; i++) { runOriginalNativeSql(); clear.run(); }
        for (int i = 0; i < WARMUP; i++) { runImprovedNativeSql(); clear.run(); }
        for (int i = 0; i < WARMUP; i++) { runJPQL();              clear.run(); }
        for (int i = 0; i < WARMUP; i++) { runQueryDSL();          clear.run(); }

        // 측정: Native (기존)
        long n1Start = System.nanoTime();
        for (int i = 0; i < N; i++) { runOriginalNativeSql(); clear.run(); }
        long n1End = System.nanoTime();

        // 측정: Native (EXISTS)
        long n2Start = System.nanoTime();
        for (int i = 0; i < N; i++) { runImprovedNativeSql(); clear.run(); }
        long n2End = System.nanoTime();

        // 측정: JPQL
        long jStart = System.nanoTime();
        for (int i = 0; i < N; i++) { runJPQL(); clear.run(); }
        long jEnd = System.nanoTime();

        // 측정: QueryDSL
        long qStart = System.nanoTime();
        for (int i = 0; i < N; i++) { runQueryDSL(); clear.run(); }
        long qEnd = System.nanoTime();

        double nativeMs      = (n1End - n1Start) / 1_000_000.0 / N;
        double nativeExistMs = (n2End - n2Start) / 1_000_000.0 / N;
        double jpqlMs        = (jEnd  - jStart)  / 1_000_000.0 / N;
        double querydslMs    = (qEnd  - qStart)  / 1_000_000.0 / N;

        System.out.println("\n=== API 스타일별 평균 실행 시간 (ms) ===");
        System.out.printf("Native SQL (기존 조인)    : %.3f ms%n", nativeMs);
        System.out.printf("Native SQL (EXISTS 최적화): %.3f ms%n", nativeExistMs);
        System.out.printf("JPQL                      : %.3f ms%n", jpqlMs);
        System.out.printf("QueryDSL                  : %.3f ms%n", querydslMs);
    }

    // ---------- 쿼리 메서드 ----------
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

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("placeId", PLACE_ID)
                .getResultList();

        List<EduRevenueDto> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Long eduId = ((Number) row[0]).longValue();
            String eduName = (String) row[1];
            BigDecimal unitPrice = toBigDecimal(row[2]);
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

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("placeId", PLACE_ID)
                .getResultList();

        List<EduRevenueDto> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Long eduId = ((Number) row[0]).longValue();
            String eduName = (String) row[1];
            BigDecimal unitPrice = toBigDecimal(row[2]);
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
        return queryFactory
                .select(Projections.constructor(
                        EduRevenueDto.class,
                        edu.id, edu.eduName, edu.eduTuition, takes.id.count()
                ))
                .from(takes)
                .join(takes.edu, edu)
                .join(takes.member, member)
                .join(member.user, user)
                .where(user.place.id.eq(PLACE_ID), user.userRole.eq(UserRole.MEMBER))
                .groupBy(edu.id, edu.eduName, edu.eduTuition)
                .fetch();
    }

    private static BigDecimal toBigDecimal(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal b) return b;
        if (o instanceof Number n) return new BigDecimal(n.toString());
        return new BigDecimal(o.toString());
    }
}
