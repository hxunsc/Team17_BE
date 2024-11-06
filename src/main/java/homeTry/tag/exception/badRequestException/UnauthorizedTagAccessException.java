package homeTry.tag.exception.badRequestException;

import homeTry.common.exception.BadRequestException;
import homeTry.tag.exception.TagErrorType;

public class UnauthorizedTagAccessException extends BadRequestException {
    public UnauthorizedTagAccessException() {super(TagErrorType.UNAUTHORIZED_TAG_ACCESS_EXCEPTION);}
}
