package homeTry.team.dto.response;

import homeTry.tag.dto.TagDTO;
import homeTry.team.model.entity.Team;
import homeTry.team.model.vo.Password;

import java.util.List;

public record TeamResponse(
        Long id,
        String teamName,
        String leaderNickname,
        String teamDescription,
        long maxParticipants,
        long currentParticipants,
        boolean hasPassword,
        List<TagDTO> tagList
) {


    public static TeamResponse of(Team team, List<TagDTO> tagList) {
        boolean hasPassword = team.getPassword().isPresent();

        return new TeamResponse(
                team.getId(),
                team.getTeamName().value(),
                team.getLeader().getNickname(),
                team.getTeamDescription().value(),
                team.getMaxParticipants().value(),
                team.getCurrentParticipants().value(),
                hasPassword,
                tagList);
    }
}