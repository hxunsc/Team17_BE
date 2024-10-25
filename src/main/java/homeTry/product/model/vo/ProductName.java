package homeTry.product.model.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductName(String value) {

    public ProductName {
        validateProductName(value);
    }

    private void validateProductName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
