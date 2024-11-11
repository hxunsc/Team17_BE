package homeTry.exerciseList.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import homeTry.exerciseList.exception.badRequestException.ExerciseTimeLimitExceededException;
import homeTry.exerciseList.model.entity.Exercise;
import homeTry.exerciseList.model.entity.ExerciseTime;
import homeTry.exerciseList.repository.ExerciseRepository;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExerciseTimeServiceTest {

    @Autowired
    private ExerciseTimeService exerciseTimeService;

    @Autowired
    private ExerciseTimeHelper exerciseTimeHelper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("test@example.com", "1234"));
        member = memberRepository.save(member);

        exercise = new Exercise("Test Exercise", member);
        exercise = exerciseRepository.save(exercise);
    }

    @Test
    @DisplayName("8시간 초과 운동에 대해 강제 종료 후 'isActive=false' 로 저장되는지")
    void stopExerciseTime_Fail() {
        // given
        ExerciseTime exerciseTime = new ExerciseTime(exercise);
        exerciseTime.startExercise();
        exerciseTime.startExerciseAt(LocalDateTime.now().minusHours(9));
        exerciseTimeHelper.saveExerciseTime(exerciseTime);

        // when & then
        ExerciseTimeLimitExceededException exception = assertThrows(
            ExerciseTimeLimitExceededException.class,
            () -> exerciseTimeService.stopExerciseTime(exercise)
        );

        assertThat(exception.getMessage()).isEqualTo("한 번에 저장되는 운동 시간은 8시간을 초과할 수 없습니다.");

        // 운동 상태 확인
        ExerciseTime updatedExerciseTime = exerciseTimeService.getExerciseTime(exercise.getExerciseId());
        assertThat(updatedExerciseTime.isActive()).isFalse();
    }

    @Test
    @DisplayName("정상적인 운동 종료")
    void stopExerciseTime_Succeed() {
        // given
        ExerciseTime exerciseTime = new ExerciseTime(exercise);
        exerciseTime.startExercise();
        exerciseTime.startExerciseAt(LocalDateTime.now().minusHours(1));
        exerciseTimeHelper.saveExerciseTime(exerciseTime);

        // when
        exerciseTimeService.stopExerciseTime(exercise);

        // then
        ExerciseTime updatedExerciseTime = exerciseTimeService.getExerciseTime(exercise.getExerciseId());
        assertThat(updatedExerciseTime.isActive()).isFalse();
        assertThat(updatedExerciseTime.getExerciseTime().toHours()).isEqualTo(1);
    }



}