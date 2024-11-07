package homeTry.member.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.member.dto.request.ChangeNicknameRequest;
import homeTry.member.dto.response.MyPageResponse;
import homeTry.member.service.MemberService;
import homeTry.member.service.MemberWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/profile")
public class MemberController {

    private final MemberService memberService;
    private final MemberWithdrawService memberWithdrawService;

    @Autowired
    public MemberController(MemberService memberService,
            MemberWithdrawService memberWithdrawService) {
        this.memberService = memberService;
        this.memberWithdrawService = memberWithdrawService;
    }

    @GetMapping
    public ResponseEntity<MyPageResponse> getMemberInfo(@LoginMember MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.getMemberInfo(memberDTO));
    }

    @PutMapping
    public ResponseEntity<Void> changeNickname(@LoginMember MemberDTO memberDTO,
            @RequestBody ChangeNicknameRequest changeNicknameRequest) {
        memberService.changeNickname(memberDTO.id(), changeNicknameRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@LoginMember MemberDTO memberDTO) {
        memberWithdrawService.withdraw(memberDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
