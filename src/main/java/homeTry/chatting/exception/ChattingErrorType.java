package homeTry.chatting.exception;

import homeTry.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum ChattingErrorType implements ErrorType {

    INVALID_TEAM_ID_EXCEPTION("Chatting400_001", HttpStatus.BAD_REQUEST, "올바르지 않은 팀 아이디 입니다."),
    INVALID_CHATTING_TOKEN_EXCEPTION("Chatting401_001", HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰입니다."),

    UNKNOWN_CHATTING_EXCEPTION("Chatting500_001", HttpStatus.INTERNAL_SERVER_ERROR,
            "예상치 못한 서버 에러입니다."),



    /*
    * 클라이언트 에게는 UNKNOWN_CHATTING_EXCEPTION과 완전히 동일한 Error 객체를 반환( "토큰은 유효하나, 회원이 DB에 없습니다." -> 이 부분을 사용자가 알면 안되기 때문 )
    * 대신 서버 로그에는 NO_SUCH_MEMBER_IN_DB_WITH_VALID_TOKEN 을 남김
    */
    NO_SUCH_MEMBER_IN_DB_WITH_VALID_TOKEN("Chatting500_002", HttpStatus.INTERNAL_SERVER_ERROR,
            "토큰은 유효하나, 회원이 DB에 없습니다.");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;


    ChattingErrorType(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
