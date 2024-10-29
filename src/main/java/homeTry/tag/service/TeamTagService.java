package homeTry.tag.service;

import homeTry.tag.dto.TeamTagDTO;
import homeTry.tag.exeception.TagNotFoundException;
import homeTry.tag.model.entity.Tag;
import homeTry.tag.model.entity.TeamTag;
import homeTry.tag.repository.TeamTagRepository;
import homeTry.team.model.entity.Team;
import homeTry.team.model.entity.TeamTagMapping;
import homeTry.team.service.TeamTagMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamTagService {

    private final TeamTagRepository teamTagRepository;
    private final TeamTagMappingService teamTagMappingService;

    public TeamTagService(TeamTagRepository teamTagRepository, TeamTagMappingService teamTagMappingService) {
        this.teamTagRepository = teamTagRepository;
        this.teamTagMappingService = teamTagMappingService;
    }

    //모든 태그 반환
    @Transactional(readOnly = true)
    public List<TeamTagDTO> getAllTeamTagList() {
        List<TeamTag> tagList = teamTagRepository.findAll();
        return tagList
                .stream()
                .map(TeamTagDTO::of)
                .toList();
    }

    //팀이 가지고 있는 tag를 찾아서 tagDTO로 반환
    @Transactional(readOnly = true)
    public List<TeamTagDTO> getTeamTagsOfTeam(Team team) {
        List<TeamTagMapping> teamTagMappingList = teamTagMappingService.getTeamTagMappingsOfTeam(team);
        return teamTagMappingList
                .stream()
                .map(teamTagMapping -> TeamTagDTO.of(teamTagMapping.getTeamTag()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TeamTag> getTeamTagList(List<Long> tagIdList) {
        return tagIdList
                .stream()
                .map(tagId -> teamTagRepository.findById(tagId)
                        .orElseThrow(() -> new TagNotFoundException()))
                .toList();
    }

}
