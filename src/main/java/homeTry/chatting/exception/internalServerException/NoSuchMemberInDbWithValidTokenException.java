package homeTry.chatting.exception.internalServerException;

import homeTry.chatting.exception.ChattingErrorType;
import homeTry.common.exception.InternalServerException;

public class NoSuchMemberInDbWithValidTokenException extends InternalServerException {

    public NoSuchMemberInDbWithValidTokenException() {
        super(ChattingErrorType.NO_SUCH_MEMBER_IN_DB_WITH_VALID_TOKEN);
    }
}
