package homeTry.product.model.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductTagName(String value) {

    public ProductTagName {
        validateProductTagName(value);
    }

    private void validateProductTagName(String productTagName) {
        if (productTagName == null || productTagName.isBlank()) {
            throw new IllegalArgumentException("태그 이름은 필수입니다.");
        }

        if (productTagName.length() > 15) {
            throw new IllegalArgumentException("태그 이름은 최대 15자까지 가능합니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
