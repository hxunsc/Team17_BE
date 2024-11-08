package homeTry.common.auth.kakaoAuth.service;

import homeTry.common.auth.kakaoAuth.dto.KakaoMemberInfoDTO;
import homeTry.member.dto.MemberDTO;
import homeTry.member.exception.badRequestException.LoginFailedException;
import homeTry.member.service.MemberService;
import org.springframework.stereotype.Service;


@Service
public class KakaoAuthService {

    private final KakaoClientService kakaoClientService;
    private final MemberService memberService;

    public KakaoAuthService(KakaoClientService kakaoClientService, MemberService memberService) {
        this.kakaoClientService = kakaoClientService;
        this.memberService = memberService;
    }

    public MemberDTO loginOrRegister(String code) {
        String accessToken = kakaoClientService.getAccessToken(code);
        KakaoMemberInfoDTO kakaoMemberInfoDTO = kakaoClientService.getMemberInfo(accessToken);

        try {
            MemberDTO memberDTOWithActualId = memberService.login(kakaoMemberInfoDTO); // -> LoginFailedException을 던질 수 있음

            memberService.setMemeberAccessToken(memberDTOWithActualId.id(), accessToken);
            return memberDTOWithActualId;
        } catch (LoginFailedException e) { //유저를 못 찾으면 회원가입

            MemberDTO memberDTOWithActualId =  memberService.register(kakaoMemberInfoDTO);

            memberService.setMemeberAccessToken(memberDTOWithActualId.id(), accessToken);
            return memberDTOWithActualId;
        }
    }
}
