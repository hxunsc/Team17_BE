package homeTry.adminTag.productTag.exception.BadRequestException;

import homeTry.adminTag.productTag.exception.ProductTagErrorType;
import homeTry.common.exception.BadRequestException;

public class ProductTagNotFoundException extends BadRequestException{
    
    public ProductTagNotFoundException() {
        super(ProductTagErrorType.PRODUCT_TAG_NOT_FOUND_EXCEPTION);
    }
}
