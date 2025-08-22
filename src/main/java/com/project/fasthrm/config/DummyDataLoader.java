package com.project.fasthrm.config;

import com.project.fasthrm.domain.type.AttendanceStatus;
import net.datafaker.Faker;
import com.project.fasthrm.domain.*;
import com.project.fasthrm.domain.type.UserRole;
import com.project.fasthrm.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final WorkerRepository workerRepository;
    private final MasterRepository masterRepository;
    private final EduRepository eduRepository;
    private final TakesRepository takesRepository;
    private final AttendanceRepository attendanceRepository;
    private final HealthCheckRepository healthCheckRepository;
    private final AcademicCheckRepository academicCheckRepository;
    private final AdvisorRepository advisorRepository;
    private final WorkTimeRepository workTimeRepository;
    private final TestRepository testRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 이미 데이터 있으면 종료
        if (placeRepository.count() > 0) {
            System.out.println("⚠️ Dummy data already exists. Skipping insertion.");
            return;
        }

        Faker faker = new Faker(new Locale("ko"));
        Random random = new Random();

        int batchSize = 1000;
        int totalCount = 1000000; // 100만건
        String[] eduDays = {"월수금", "화목", "토일", "월화수", "수목금", "월수", "매일"};

        // ===== Place 생성 =====
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Place place = Place.builder()
                    .placeName(faker.company().name() + " 지점")
                    .placeType(i % 2 == 0 ? "gym" : "academy")
                    .build();
            em.persist(place);
            places.add(place);
        }

        // ===== User(Member/Worker/Master) 생성 =====
        List<Member> members = new ArrayList<>();
        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < totalCount; i++) {
            Place place = places.get(random.nextInt(places.size()));
            UserRole role = UserRole.values()[random.nextInt(UserRole.values().length)];

            User user = User.builder()
                    .place(place)
                    .username("user" + i + "_" + faker.bothify("??##"))
                    .password("$2a$10$hashedPW")
                    .userRealName(faker.name().fullName())
                    .userPhoneNumber("010-" + faker.numerify("####-####"))
                    .userRole(role)
                    .build();
            em.persist(user);

            switch (role) {
                case MEMBER -> {
                    Member member = Member.builder()
                            .user(user)
                            .memberSignificant(faker.lorem().sentence())
                            .build();
                    em.persist(member);
                    members.add(member);
                }
                case WORKER -> {
                    Worker worker = Worker.builder()
                            .user(user)
                            .workerSalary(random.nextInt(3000000) + 2000000)
                            .approvedForTuitionUpdate(false)
                            .build();
                    em.persist(worker);
                    workers.add(worker);
                }
                case MASTER -> {
                    Master master = Master.builder()
                            .user(user)
                            .build();
                    em.persist(master);
                }
            }

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
            }
        }

        // ===== Edu 생성 =====
        List<Edu> edus = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            Edu edu = Edu.builder()
                    .place(places.get(random.nextInt(places.size())))
                    .worker(workers.get(random.nextInt(workers.size())))
                    .eduName(faker.company().name() + " " + faker.job().title() + " 교육")
                    .eduDay(eduDays[random.nextInt(eduDays.length)])
                    .eduStart(LocalDateTime.now().minusWeeks(random.nextInt(52)))
                    .eduEnd(LocalDateTime.now())
                    .eduTuition(BigDecimal.valueOf(random.nextInt(400000) + 100000))
                    .eduRoomName((100 + random.nextInt(900)) + "호")
                    .build();
            em.persist(edu);
            edus.add(edu);

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
            }
        }

        // ===== Takes / Attendance / 기타 데이터 생성 =====
        for (int i = 0; i < 100000; i++) {
            Member member = members.get(random.nextInt(members.size()));
            Worker worker = workers.get(random.nextInt(workers.size()));
            Edu edu = edus.get(random.nextInt(edus.size()));

            // 수강 등록
            em.persist(Takes.builder()
                    .member(member)
                    .edu(edu)
                    .registeredAt(LocalDate.now().minusDays(random.nextInt(365)))
                    .build());

            // 회원 Attendance 생성
            if (random.nextDouble() < 0.7) {
                AttendanceStatus status = getRandomAttendanceStatus(random);
                LocalDate randomDate = LocalDate.now().minusDays(random.nextInt(100));

                em.persist(Attendance.builder()
                        .member(member)
                        .edu(edu)
                        .attendanceDatetime(randomDate.atTime(9, 0))
                        .isAttended(status)
                        .build());
            }

            // 직원 WorkTime 생성
            AttendanceStatus workStatus = getRandomAttendanceStatus(random);
            em.persist(WorkTime.builder()
                    .worker(worker)
                    .worktimeDay(LocalDate.now().minusDays(random.nextInt(60)))
                    .type(workStatus) // Enum을 String으로 저장
                    .time("0" + (8 + random.nextInt(2)) + ":00~18:00")
                    .start(LocalDateTime.now().minusHours(8))
                    .end(LocalDateTime.now())
                    .build());

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
        System.out.println("✅ 100만건 Dummy 데이터 삽입 완료 (중복 없음)");
    }

    private AttendanceStatus getRandomAttendanceStatus(Random random) {
        double rand = random.nextDouble();
        if (rand < 0.6) {
            return AttendanceStatus.PRESENT;
        } else if (rand < 0.8) {
            return AttendanceStatus.LATE;
        } else {
            return AttendanceStatus.ABSENT;
        }
    }
}
