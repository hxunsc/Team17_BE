package homeTry.adminTag.teamTag.dto.response;

import java.util.List;

import homeTry.adminTag.teamTag.dto.TeamTagDTO;

public record TeamTagResponse(
    List<TeamTagDTO> teamTags
) {
}
