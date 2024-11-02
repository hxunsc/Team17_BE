package homeTry.team.dto.response;

import homeTry.tag.dto.TeamTagDTO;

import java.util.List;

public record TagListResponse(
        List<TeamTagDTO> genderTagList,
        List<TeamTagDTO> ageTagList,
        List<TeamTagDTO> exerciseIntensityTagList
) {
}