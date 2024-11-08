package homeTry.team.exception;

import homeTry.common.exception.BadRequestException;

public class TeamParticipantsFullException extends BadRequestException {
    public TeamParticipantsFullException() {
        super(TeamErrorType.TEAM_PARTICIPANTS_FULLED_EXCEPTION);
    }
}
