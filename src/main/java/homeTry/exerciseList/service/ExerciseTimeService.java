package homeTry.exerciseList.service;

import homeTry.common.constants.DateTimeUtil;
import homeTry.exerciseList.dto.response.ExerciseResponse;
import homeTry.exerciseList.exception.badRequestException.DailyExerciseTimeLimitExceededException;
import homeTry.exerciseList.exception.badRequestException.ExerciseAlreadyStartedException;
import homeTry.exerciseList.exception.badRequestException.ExerciseNotStartedException;
import homeTry.exerciseList.exception.badRequestException.ExerciseTimeLimitExceededException;
import homeTry.exerciseList.model.entity.Exercise;
import homeTry.exerciseList.model.entity.ExerciseTime;
import homeTry.exerciseList.repository.ExerciseTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExerciseTimeService {

    private final ExerciseTimeRepository exerciseTimeRepository;

    public ExerciseTimeService(ExerciseTimeRepository exerciseTimeRepository) {
        this.exerciseTimeRepository = exerciseTimeRepository;
    }

    @Transactional
    public void startExerciseTime(Exercise exercise) {
        // 실행 중인 운동이 있는지
        long activeExerciseCount = exerciseTimeRepository.countActiveExercisesByMemberId(
            exercise.getMember().getId());
        if (activeExerciseCount > 0) {
            throw new ExerciseAlreadyStartedException();
        }

        // 현재 운동 상태 확인
        ExerciseTime currentExerciseTime = getExerciseTime(exercise.getExerciseId());
        currentExerciseTime.startExercise();
        saveExerciseTime(currentExerciseTime);
    }

    @Transactional
    public void stopExerciseTime(Exercise exercise) {
        ExerciseTime currentExerciseTime = getExerciseTime(exercise.getExerciseId());

        if (currentExerciseTime == null || !currentExerciseTime.isActive()) {
            throw new ExerciseNotStartedException();
        }

        // 하루 최대 12시간, 한 번에 저장되는 최대 시간 8시간을 넘었는지 확인
        validateExerciseDurationLimits(currentExerciseTime);
        currentExerciseTime.stopExercise();
    }

    private void validateExerciseDurationLimits(ExerciseTime exerciseTime) {
        Duration timeElapsed = Duration.between(exerciseTime.getStartTime(), LocalDateTime.now());
        Duration totalTime = exerciseTime.getExerciseTime().plus(timeElapsed);

        // 한 번 운동한 시간이 8시간을 초과한 경우
        if (timeElapsed.compareTo(Duration.ofHours(8)) > 0) {
            exerciseTime.stopExerciseWithoutSavingTime();  // 기록 저장 없이 강제 종료
            throw new ExerciseTimeLimitExceededException();
        }

        // 하루 총 운동 시간이 12시간을 초과한 경우
        if (totalTime.compareTo(Duration.ofHours(12)) > 0) {
            exerciseTime.stopExerciseWithoutSavingTime();
            throw new DailyExerciseTimeLimitExceededException();
        }
    }

    @Transactional
    public void saveExerciseTime(ExerciseTime exerciseTime) {
        exerciseTimeRepository.save(exerciseTime);
    }

    @Transactional
    public void resetDailyExercise(ExerciseTime exerciseTime) {
        exerciseTime.resetDailyExercise();
    }

    @Transactional
    public ExerciseTime getExerciseTime(Long exerciseId) {
        return exerciseTimeRepository.findByExerciseId(exerciseId)
            .orElse(null);
    }

    // 메인페이지, 팀 랭킹
    // 당일의 운동 전체 시간 반환
    @Transactional(readOnly = true)
    public Long getExerciseTimesForToday(Long memberId) {
        LocalDateTime startOfDay = DateTimeUtil.getStartOfDay(LocalDate.now());
        LocalDateTime endOfDay = DateTimeUtil.getEndOfDay(LocalDate.now());

        // 해당 멤버의 당일 운동 시간 목록 조회
        List<ExerciseTime> exerciseTimes = exerciseTimeRepository.findValidExerciseTimesForMemberOnDate(
            memberId, startOfDay, endOfDay);

        // 운동 시간 총 합
        return exerciseTimes
            .stream()
            .map(ExerciseTime::getExerciseTime)
            .reduce(Duration.ZERO, Duration::plus)
            .toMillis();
    }

    // 메인 페이지 운동 리스트 반환
    @Transactional(readOnly = true)
    public List<ExerciseResponse> getExerciseResponsesForToday(Long memberId) {
        LocalDateTime startOfDay = DateTimeUtil.getStartOfDay(LocalDate.now());
        LocalDateTime endOfDay = DateTimeUtil.getEndOfDay(LocalDate.now());

        // 삭제된 운동 제외하고 운동 시간 가져오기
        List<ExerciseTime> exerciseTimes = exerciseTimeRepository.findValidExerciseTimesForMemberOnDate(
            memberId, startOfDay, endOfDay);

        return exerciseTimes
            .stream()
            .map(ExerciseResponse::from)
            .toList();
    }

    @Transactional
    public void deleteExerciseTimesByExerciseId(Long exerciseId) {
        exerciseTimeRepository.deleteByExerciseId(exerciseId);
    }

}
