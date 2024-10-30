package homeTry.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
    @NotBlank
    String imageUrl,
    @NotBlank
    String productUrl,
    @NotBlank
    String name,
    @NotNull
    Long price,
    @NotBlank
    String storeName
) {

}
