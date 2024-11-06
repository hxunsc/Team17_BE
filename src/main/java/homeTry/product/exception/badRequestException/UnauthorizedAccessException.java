package homeTry.product.exception.badRequestException;

import homeTry.common.exception.BadRequestException;
import homeTry.product.exception.ProductErrorType;

public class UnauthorizedAccessException  extends BadRequestException {

    public UnauthorizedAccessException() {
        super(ProductErrorType.UNAUTHORIZED_ACCESS_EXCEPTION);
    }

}
