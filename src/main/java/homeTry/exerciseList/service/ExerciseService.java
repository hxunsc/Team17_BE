package homeTry.exerciseList.service;

import homeTry.exerciseList.dto.request.ExerciseRequest;
import homeTry.exerciseList.event.ExerciseEventPublisher;
import homeTry.exerciseList.exception.badRequestException.*;
import homeTry.exerciseList.model.entity.Exercise;
import homeTry.exerciseList.model.entity.ExerciseTime;
import homeTry.exerciseList.repository.ExerciseRepository;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseEventPublisher exerciseEventPublisher;
    private final ExerciseTimeService exerciseTimeService;
    private final MemberService memberService;

    public ExerciseService(ExerciseRepository exerciseRepository,
        ExerciseEventPublisher exerciseEventPublisher,
        ExerciseTimeService exerciseTimeService, MemberService memberService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseEventPublisher = exerciseEventPublisher;
        this.exerciseTimeService = exerciseTimeService;
        this.memberService = memberService;
    }

    @Transactional
    public void createExercise(ExerciseRequest request, MemberDTO memberDTO) {
        Member foundMember = memberService.getMemberEntity(memberDTO.id());

        Exercise exercise = new Exercise(request.exerciseName(), foundMember);
        exerciseRepository.save(exercise);

        exerciseEventPublisher.publishCreationEvent(exercise);
    }

    @Transactional
    public void deleteExercise(Long exerciseId, MemberDTO memberDTO) {
        Exercise exercise = getExerciseById(exerciseId);
        validateMemberPermission(exercise, memberDTO);

        if (!exercise.getMember().getId().equals(memberDTO.id())) {
            throw new NoExercisePermissionException();
        }

        ExerciseTime currentExerciseTime = exerciseTimeService.getExerciseTime(
            exercise.getExerciseId());
        if (currentExerciseTime != null && currentExerciseTime.isActive()) {
            throw new ExerciseInProgressException();
        }

        exercise.markAsDeprecated(); // isDeprecated 값을 true로 설정
    }

    @Transactional
    public void startExercise(Long exerciseId, MemberDTO memberDTO) {
        Exercise exercise = getExerciseById(exerciseId);
        validateMemberPermission(exercise, memberDTO);

        // 삭제한 운동을 시작하려는 경우
        if (exercise.isDeprecated()) {
            throw new ExerciseDeprecatedException();
        }

        exerciseTimeService.startExerciseTime(exercise);
    }

    @Transactional
    public void stopExercise(Long exerciseId, MemberDTO memberDTO) {
        Exercise exercise = getExerciseById(exerciseId);
        validateMemberPermission(exercise, memberDTO);

        ExerciseTime currentExerciseTime = exerciseTimeService.getExerciseTime(
                exercise.getExerciseId());

        if (currentExerciseTime == null || !currentExerciseTime.isActive()) {
            throw new ExerciseNotStartedException();
        }

        // 하루 최대 12시간, 한 번에 저장되는 최대 시간 8시간을 넘었는지 확인
        exerciseTimeService.validateExerciseDurationLimits(currentExerciseTime);

        currentExerciseTime.stopExercise();
    }

    private Exercise getExerciseById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(ExerciseNotFoundException::new);
    }

    // 해당 운동이 해당 회원의 것인지 검증
    private void validateMemberPermission(Exercise exercise, MemberDTO memberDTO) {
        if (!exercise.getMember().getId().equals(memberDTO.id())) {
            throw new NoExercisePermissionException();
        }
    }

    @Transactional(readOnly = true)
    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

}
