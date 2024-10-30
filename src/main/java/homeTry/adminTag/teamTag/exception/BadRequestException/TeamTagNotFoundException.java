package homeTry.adminTag.teamTag.exception.BadRequestException;

import homeTry.adminTag.teamTag.exception.TeamTagErrorType;
import homeTry.common.exception.BadRequestException;

public class TeamTagNotFoundException extends BadRequestException {
    
    public TeamTagNotFoundException() {
        super(TeamTagErrorType.TEAM_TAG_NOT_FOUND_EXCEPTION);
    }
    
}
