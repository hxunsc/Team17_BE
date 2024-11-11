package homeTry.member.service;

import homeTry.diary.service.DiaryService;
import homeTry.exerciseList.service.ExerciseService;
import homeTry.member.dto.MemberDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberWithdrawService {

    private final ExerciseService exerciseService;
    private final MemberService memberService;
    private final DiaryService diaryService;
    private final MemberTeamWithdrawService memberTeamWithdrawService;

    public MemberWithdrawService(ExerciseService exerciseService, MemberService memberService,
            DiaryService diaryService, MemberTeamWithdrawService memberTeamWithdrawService) {
        this.exerciseService = exerciseService;
        this.memberService = memberService;
        this.diaryService = diaryService;
        this.memberTeamWithdrawService = memberTeamWithdrawService;
    }

    @Transactional
    public void withdraw(MemberDTO memberDTO) {
        Long withdrawMemberId = memberDTO.id();

        memberTeamWithdrawService.withdrawTeamByWithdrawMember(withdrawMemberId);
        exerciseService.deleteAllExercisesByMemberId(withdrawMemberId);
        diaryService.deleteByMember(withdrawMemberId);
        memberService.withdrawMember(withdrawMemberId);
    }
}