package homeTry.mainPage.exception.BadRequestException;

import homeTry.exception.BadRequestException;
import homeTry.mainPage.exception.MainPageErrorType;

public class InvalidDateException extends BadRequestException {

    public InvalidDateException() {
        super(MainPageErrorType.INVALID_DATE_EXCEPTION);
    }
}
