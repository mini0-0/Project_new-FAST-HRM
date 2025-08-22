package com.project.fasthrm.repository;
import com.project.fasthrm.domain.*;
import com.project.fasthrm.domain.type.UserRole;
import com.project.fasthrm.domain.type.AttendanceStatus;
import com.project.fasthrm.dto.response.MemberTodayLessonDto;
import com.project.fasthrm.dto.response.MemberWeeklyAttendanceDto;
import com.project.fasthrm.repository.query.MemberMainQueryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("MemberMain - Query 테스트")
class MemberMainQueryRepositoryTest {

    @Autowired private PlaceRepository placeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private WorkerRepository workerRepository;
    @Autowired private EduRepository eduRepository;
    @Autowired private TakesRepository takesRepository;
    @Autowired private MemberMainQueryRepository memberMainQueryRepository;
    @Autowired private EntityManager em;


    private Place savedPlace;
    private Member savedMember;
    // 테스트 기준 날짜: 2025-04-17
    private Edu savedEdu;
    private final LocalDate testDate = LocalDate.of(2025, 4, 17);

    @BeforeEach
    void setUp() {
        // Place 생성
        savedPlace = placeRepository.save(Place.builder()
                .placeName("학원A")
                .placeType("AC")
                .build()
        );

        // Member 유저 생성
        User memberUser = userRepository.save(User.builder()
                .username("member1")
                .password("pw")
                .userRealName("홍길동")
                .userPhoneNumber("010-0000-0000")
                .place(savedPlace)
                .userRole(UserRole.MEMBER)
                .build()
        );
        savedMember = memberRepository.save(Member.builder()
                .user(memberUser)
                .memberSignificant("특이사항 없음")
                .build()
        );

        // Worker 유저 생성
        User workerUser = userRepository.save(User.builder()
                .username("worker1")
                .password("pw")
                .userRealName("강사김")
                .userPhoneNumber("010-9999-9999")
                .place(savedPlace)
                .userRole(UserRole.WORKER)
                .build()
        );
        Worker savedWorker = workerRepository.save(Worker.builder()
                .user(workerUser)
                .approvedForTuitionUpdate(false)
                .build()
        );

        // 수업(Edu) 생성: 요일 '목' 포함
        LocalDateTime startTime = testDate.atStartOfDay().plusHours(9);
        savedEdu = eduRepository.save(Edu.builder()
                .eduName("수학 수업")
                .eduDay(testDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                .eduStart(startTime)
                .eduEnd(startTime.plusHours(1))
                .eduRoomName("101호")
                .place(savedPlace)
                .worker(savedWorker)
                .build()
        );

        // 이번 달(4월) 등록 이력
        takesRepository.save(Takes.builder()
                .edu(savedEdu)
                .member(savedMember)
                .registeredAt(LocalDate.of(2025, 4, 1))
                .build()
        );
        takesRepository.save(Takes.builder()
                .edu(savedEdu)
                .member(savedMember)
                .registeredAt(testDate)
                .build()
        );

        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("이번 달 수강등록 된 오늘 요일 수업 조회")
    void findTodayLessons() {
        List<MemberTodayLessonDto> dtoList = memberMainQueryRepository
                .findTodayLessons(savedPlace.getId(), savedMember.getId(), testDate);

        // 오늘 요일(목)인 등록 건 두 개가 반환되어야 한다
        assertThat(dtoList).hasSize(2);
        dtoList.forEach(dto -> {
            assertThat(dto.getDate().getMonthValue()).isEqualTo(4);
            assertThat(dto.getDayOfWeek()).contains(testDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            assertThat(dto.getEduName()).isEqualTo("수학 수업");
            assertThat(dto.getWorkerName()).isEqualTo("강사김");
            assertThat(dto.getEduRoomName()).isEqualTo("101호");

        });
    }



    @Test
    @DisplayName("이번주(일주일) 출석 현황 조회 ")
    void findWeeklyAttendances() {
        // given
        LocalDate startOfWeek = testDate.minusDays(testDate.getDayOfWeek().getValue() -1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        takesRepository.save(Takes.builder()
                .edu(savedEdu)
                .member(savedMember)
                .registeredAt(startOfWeek.plusDays(1))
                .build()
        );

        takesRepository.save(Takes.builder()
                .edu(savedEdu)
                .member(savedMember)
                .registeredAt(startOfWeek.plusDays(3))
                .build()
        );

        em.flush();
        em.clear();


        // when & then
        List<MemberWeeklyAttendanceDto> dtoList = memberMainQueryRepository
                .findWeeklyAttendances(savedPlace.getId(), savedMember.getId(), testDate);

        assertThat(dtoList).hasSize(7); // 월~일 7일


        for (int i = 0; i < dtoList.size(); i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);

            AttendanceStatus status = dtoList.get(i).getIsAttended();
            if (status == null) {
                status = AttendanceStatus.ABSENT; // 기본 결석 처리
            }

            if (currentDate.equals(startOfWeek.plusDays(1)) || currentDate.equals(startOfWeek.plusDays(3))) {
                assertThat(status)
                        .as("%s 요일 출석 상태", currentDate.getDayOfWeek())
                        .isEqualTo(AttendanceStatus.PRESENT);
            } else {
                assertThat(status)
                        .as("%s 요일 출석 상태", currentDate.getDayOfWeek())
                        .isEqualTo(AttendanceStatus.ABSENT);
            }
        }
    }
}
