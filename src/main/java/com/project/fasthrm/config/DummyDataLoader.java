package com.project.fasthrm.config;

import net.datafaker.Faker;
import com.project.fasthrm.domain.*;
import com.project.fasthrm.domain.type.UserRole;
import com.project.fasthrm.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Profile("dev")
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
        Faker faker = new Faker(new Locale("ko"));
        Random random = new Random();

        int batchSize = 1000;
        int totalCount = 10000;

        String[] eduDays = {"월수금", "화목", "토일", "월화수", "수목금", "매일"};

        // 1. Place 생성
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Place place = Place.builder()
                    .placeName(faker.company().name() + " 지점")
                    .placeType(i % 2 == 0 ? "gym" : "academy")
                    .build();
            em.persist(place);
            places.add(place);
        }

        // 2. 사용자, 멤버, 워커, 마스터
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

        // 3. Edu 생성
        List<Edu> edus = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Edu edu = Edu.builder()
                    .place(places.get(random.nextInt(places.size())))
                    .worker(workers.get(random.nextInt(workers.size())))
                    .eduName(faker.company().name() + " " + faker.job().title() + " 교육")
                    .eduDay(eduDays[random.nextInt(eduDays.length)])
                    .eduStart(LocalDateTime.now().minusWeeks(random.nextInt(52)))
                    .eduEnd(LocalDateTime.now())
                    .eduTuition(BigDecimal.valueOf(random.nextInt(400000) + 100000))
                    .build();
            em.persist(edu);
            edus.add(edu);
        }

        // 4. Takes, Attendance, HealthCheck, AcademicCheck, Advisor, Test, WorkTime
        for (int i = 0; i < 10000; i++) {
            Member member = members.get(random.nextInt(members.size()));
            Worker worker = workers.get(random.nextInt(workers.size()));
            Edu edu = edus.get(random.nextInt(edus.size()));

            Takes takes = Takes.builder()
                    .member(member)
                    .edu(edu)
                    .registeredAt(LocalDate.now().minusDays(random.nextInt(365)))
                    .build();
            em.persist(takes);

            Attendance attendance = Attendance.builder()
                    .member(member)
                    .attendanceDatetime(LocalDateTime.now().minusDays(random.nextInt(100)))
                    .build();
            em.persist(attendance);

            HealthCheck hc = HealthCheck.builder()
                    .member(member)
                    .hcSex(random.nextBoolean() ? "남" : "여")
                    .hcHeight(150f + random.nextFloat() * 40)
                    .hcWeight(45f + random.nextFloat() * 40)
                    .hcDate(LocalDateTime.now().minusDays(random.nextInt(300)))
                    .hcPurpose(faker.lorem().word())
                    .hcTotalbodywater(BigDecimal.valueOf(50 + random.nextDouble() * 10))
                    .hcProtein(BigDecimal.valueOf(10 + random.nextDouble() * 5))
                    .hcMinerals(BigDecimal.valueOf(3 + random.nextDouble() * 2))
                    .hcBodyfatmass(BigDecimal.valueOf(15 + random.nextDouble() * 10))
                    .hcSkeletalmusclemass(BigDecimal.valueOf(25 + random.nextDouble() * 5))
                    .hcReport(faker.lorem().sentence())
                    .build();
            em.persist(hc);

            AcademicCheck ac = AcademicCheck.builder()
                    .member(member)
                    .acGrade(random.nextInt(6))
                    .acClass("" + (char)(random.nextInt(26) + 'A'))
                    .acSchool(faker.university().name())
                    .acParent(faker.name().fullName())
                    .acDate(LocalDateTime.now().minusDays(random.nextInt(300)))
                    .build();
            em.persist(ac);

            Advisor advisor = Advisor.builder()
                    .member(member)
                    .worker(worker)
                    .build();
            em.persist(advisor);

            Test test = Test.builder()
                    .takes(takes)
                    .testDay(LocalDate.now().minusDays(random.nextInt(60)))
                    .testResult(faker.letterify("?"))
                    .testName(faker.educator().course())
                    .build();
            em.persist(test);

            WorkTime wt = WorkTime.builder()
                    .worker(worker)
                    .worktimeDay(LocalDate.now().minusDays(random.nextInt(60)))
                    .type(random.nextBoolean() ? "정상" : "지각")
                    .time("0" + (8 + random.nextInt(2)) + ":00~18:00")
                    .start(LocalDateTime.now().minusHours(8))
                    .end(LocalDateTime.now())
                    .build();
            em.persist(wt);

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
        System.out.println("✅ Dummy data insertion complete.");
    }
}
