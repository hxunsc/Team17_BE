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
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExerciseTimeService {

    private final ExerciseTimeRepository exerciseTimeRepository;
    private final ExerciseTimeHelper exerciseTimeHelper;

    public ExerciseTimeService(ExerciseTimeRepository exerciseTimeRepository,
        ExerciseTimeHelper exerciseTimeHelper) {
        this.exerciseTimeRepository = exerciseTimeRepository;
        this.exerciseTimeHelper = exerciseTimeHelper;
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
        exerciseTimeHelper.saveExerciseTime(currentExerciseTime);
    }

    // 해당 예외 발생 시 롤백하지 않고 isActive=false 상태가 저장되도록 함
    @Transactional(noRollbackFor = {ExerciseTimeLimitExceededException.class,
        DailyExerciseTimeLimitExceededException.class})
    public void stopExerciseTime(Exercise exercise) {
        ExerciseTime currentExerciseTime = getExerciseTime(exercise.getExerciseId());

        if (currentExerciseTime == null || !currentExerciseTime.isActive()) {
            throw new ExerciseNotStartedException();
        }

        // 하루 최대 12시간, 한 번에 저장되는 최대 시간 8시간을 넘었는지 확인
        try {
            validateExerciseDurationLimits(currentExerciseTime);
        } catch (ExerciseTimeLimitExceededException | DailyExerciseTimeLimitExceededException e) {
            currentExerciseTime.stopExerciseWithoutSavingTime();  // 기록 저장 없이 강제 종료
            throw e;
        }

        // 정상 종료
        currentExerciseTime.stopExercise();
    }

    private void validateExerciseDurationLimits(ExerciseTime exerciseTime) {
        Duration timeElapsed = Duration.between(exerciseTime.getStartTime(), LocalDateTime.now());
        Duration totalTime = exerciseTime.getExerciseTime().plus(timeElapsed);

        // 한 번 운동한 시간이 8시간을 초과한 경우
        if (timeElapsed.compareTo(Duration.ofHours(8)) > 0) {
            throw new ExerciseTimeLimitExceededException();
        }

        // 하루 총 운동 시간이 12시간을 초과한 경우
        if (totalTime.compareTo(Duration.ofHours(12)) > 0) {
            throw new DailyExerciseTimeLimitExceededException();
        }
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
        List<ExerciseTime> exerciseTimes = getExerciseTimesListForToday(memberId);

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
        List<ExerciseTime> exerciseTimes = getExerciseTimesListForToday(memberId);

        return exerciseTimes
            .stream()
            .map(ExerciseResponse::from)
            .toList();
    }

    // 특정 회원의 오늘 운동 시간 목록 조회
    private List<ExerciseTime> getExerciseTimesListForToday(Long memberId) {
        LocalDateTime[] adjustedDateRange = DateTimeUtil.getAdjustedDateRange();
        LocalDateTime startOfDay = adjustedDateRange[0];
        LocalDateTime endOfDay = adjustedDateRange[1];

        return exerciseTimeRepository.findValidExerciseTimesForMemberOnDate(memberId, startOfDay,
            endOfDay);
    }

    @Transactional
    public void deleteExerciseTimesByExerciseId(Long exerciseId) {
        exerciseTimeRepository.deleteByExerciseId(exerciseId);
    }

}
