package homeTry.adminTag.productTag.model.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductTagName(String value) {

    public ProductTagName {
        validateProductTagName(value);
    }

    private void validateProductTagName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("상품태그 이름값은 필수입니다");
        }
        if (value.length() > 15) {
            throw new IllegalArgumentException("상품태그의 길이는 최대 15자 입니다");
        }
    }
}
