package homeTry.product.exception.badRequestException;

import homeTry.common.exception.BadRequestException;
import homeTry.product.exception.ProductErrorType;

public class ForbiddenProductAccessException extends BadRequestException {

    public ForbiddenProductAccessException() {
        super(ProductErrorType.FORBIDDEN_PRODUCT_ACCESS_EXCEPTION);
    }

}
