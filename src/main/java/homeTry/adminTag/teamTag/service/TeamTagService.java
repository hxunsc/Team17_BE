package homeTry.adminTag.teamTag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import homeTry.adminTag.teamTag.dto.TeamTagDTO;
import homeTry.adminTag.teamTag.dto.request.TeamTagRequest;
import homeTry.adminTag.teamTag.dto.response.TeamTagResponse;
import homeTry.adminTag.teamTag.exception.BadRequestException.TeamTagNotFoundException;
import homeTry.adminTag.teamTag.model.entity.TeamTag;
import homeTry.adminTag.teamTag.repository.TeamTagRepository;

@Service
public class TeamTagService {

    private final TeamTagRepository teamTagRepository;

    public TeamTagService(TeamTagRepository teamTagRepository) {
        this.teamTagRepository = teamTagRepository;
    }

    public TeamTagResponse getTeamTagList() {
        
        List<TeamTagDTO> teamTagList = teamTagRepository.findAll()
                .stream()
                .map(TeamTagDTO::from)
                .toList();

        return new TeamTagResponse(teamTagList);
    }
    
    public void addTeamTag(TeamTagRequest teamTagRequest){

        teamTagRepository.save(
            new TeamTag(
                teamTagRequest.teamTagName(), 
                teamTagRequest.teamTagAttribute())
        );
    }

    public void deleteTeamTag(Long teamTagId){
        
        TeamTag teamTag = teamTagRepository.findById(teamTagId)
            .orElseThrow(() -> new TeamTagNotFoundException());

        teamTagRepository.delete(teamTag);
    }
}
