package homeTry.member.dto;

import homeTry.member.model.entity.Member;
import homeTry.member.model.enums.Role;

public record MemberDTO(
        Long id,
        String email,
        String nickname,
        Role role
) {

    public MemberDTO(String email, String nickname, Role role) {
        this(0L, email, nickname, role);
    }

    public static MemberDTO from(Member member) {
        return new MemberDTO(member.getId(), member.getEmail(), member.getNickname(), member.getRole());
    }

    public Member toEntity() {
        return new Member(this.email, this.nickname);
    }

}
