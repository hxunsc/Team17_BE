package homeTry.exerciseList.service;

import static org.junit.jupiter.api.Assertions.*;

import homeTry.exerciseList.model.entity.Exercise;
import homeTry.exerciseList.model.entity.ExerciseHistory;
import homeTry.exerciseList.model.entity.ExerciseTime;
import homeTry.exerciseList.repository.ExerciseHistoryRepository;
import homeTry.exerciseList.repository.ExerciseRepository;
import homeTry.exerciseList.repository.ExerciseTimeRepository;
import homeTry.member.model.entity.Member;
import homeTry.member.model.vo.Email;
import homeTry.member.repository.MemberRepository;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ExerciseSchedulerServiceTest {

    @Autowired
    private ExerciseSchedulerService exerciseSchedulerService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseTimeRepository exerciseTimeRepository;

    @Autowired
    private ExerciseHistoryRepository exerciseHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        exerciseHistoryRepository.deleteAll();
        exerciseTimeRepository.deleteAll();
        exerciseRepository.deleteAll();

        Member member = memberRepository.save(new Member("test@example.com", "1234"));

        Exercise exercise = new Exercise("헬스", member);
        exerciseRepository.save(exercise);

        ExerciseTime exerciseTime = new ExerciseTime(exercise);
        exerciseTime.startExercise();
        exerciseTimeRepository.save(exerciseTime);
    }

    @Test
    @DisplayName("스케줄링 작업 - 새벽 3시에 운동 기록 저장 및 시간 초기화")
    void testSaveAllExerciseHistoryAt3AM() {
        exerciseSchedulerService.saveAllExerciseHistoryAt3AM();

        List<ExerciseTime> exerciseTimes = exerciseTimeRepository.findAll();
        Member member = memberRepository.findByEmail(new Email("test@example.com")).orElseThrow();
        List<ExerciseHistory> exerciseHistories = exerciseHistoryRepository.findAll();

        // 3시 이후 운동이 강제 종료 되는가
        assertFalse(exerciseTimes.get(0).isActive());

        // exerciseTime이 0으로 초기화 되는가
        assertEquals(Duration.ZERO, exerciseTimes.get(0).getExerciseTime());

        // 운동 기록이 있는 멤버의 출석일이 증가하는가
        assertTrue(member.getExerciseAttendanceDate() > 0);

        // ExerciseTime -> ExerciseHistory로 저장이 되는가
        assertEquals(1, exerciseHistories.size());

        // ExerciseHistory에 저장된 운동과 원래 운동 이름이 같은가
        assertEquals("헬스", exerciseHistories.get(0).getExercise().getExerciseName());
    }

}