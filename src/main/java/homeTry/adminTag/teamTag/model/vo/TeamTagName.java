package homeTry.adminTag.teamTag.model.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record TeamTagName(String value) {
    
    public TeamTagName {
        validateTeamTagName(value);
    }

    private void validateTeamTagName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("팀태그 이름값은 필수입니다");
        }
        if (value.length() > 15) {
            throw new IllegalArgumentException("팀태그 이름의 길이는 최대 15자 입니다");
        }
    }
}
