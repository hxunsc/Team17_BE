package homeTry.team.exception;

import homeTry.common.exception.BadRequestException;

public class TeamLeaderCannotWithdrawException extends BadRequestException {
    public TeamLeaderCannotWithdrawException() {
        super(TeamErrorType.TEAM_LEADER_CANNOT_WITHDRAW_EXCEPTION);
    }
}
