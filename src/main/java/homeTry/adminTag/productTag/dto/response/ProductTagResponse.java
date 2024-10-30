package homeTry.adminTag.productTag.dto.response;

import java.util.List;

import homeTry.adminTag.productTag.dto.ProductTagDto;

public record ProductTagResponse(
    List<ProductTagDto> productTags
) {
}
