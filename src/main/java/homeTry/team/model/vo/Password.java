package homeTry.team.model.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public record Password(String value) {

    public Password {
        validatePassword(value);
    }

    private void validatePassword(String value) {
        if (value == null)
            return;

        if (value.isBlank()) {
            throw new IllegalArgumentException("password값은 비어있을 수 없습니다");
        }
        if (value.length() < 4 || value.length() > 16) {
            throw new IllegalArgumentException("password의 길이는 최소 4글자 이상 최대 16글자 이하입니다");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Password)) {
            return false;
        }

        Password password = (Password) object;
        return Objects.equals(this.value, password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
