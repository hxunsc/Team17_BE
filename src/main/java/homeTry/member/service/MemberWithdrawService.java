package homeTry.member.service;

import homeTry.diary.service.DiaryService;
import homeTry.exerciseList.service.ExerciseService;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberWithdrawService {

    private final ExerciseService exerciseService;
    private final MemberService memberService;
    private final DiaryService diaryService;

    public MemberWithdrawService(ExerciseService exerciseService, MemberService memberService,
            DiaryService diaryService) {
        this.exerciseService = exerciseService;
        this.memberService = memberService;
        this.diaryService = diaryService;
    }

    public void withdraw(MemberDTO memberDTO) {
        Member member = memberService.getMemberEntity(memberDTO.id());
        Long memberId = member.getId();

        exerciseService.deleteAllExercisesByMemberId(memberId);
        diaryService.deleteByMember(memberId);
        memberService.withdrawMember(memberId);
    }
}
