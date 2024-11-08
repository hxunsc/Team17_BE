package homeTry.tag.teamTag.service;

import homeTry.member.dto.MemberDTO;
import homeTry.member.service.MemberService;
import homeTry.tag.exception.badRequestException.ForbiddenTagAccessException;
import homeTry.tag.model.vo.TagName;
import homeTry.tag.teamTag.dto.AllTeamTagDTO;
import homeTry.tag.teamTag.dto.TeamTagDTO;
import homeTry.tag.teamTag.dto.request.TeamTagRequest;
import homeTry.tag.teamTag.dto.response.TeamTagResponse;
import homeTry.tag.teamTag.exception.BadRequestException.TeamTagAlreadyExistsException;
import homeTry.tag.teamTag.exception.BadRequestException.TeamTagNotFoundException;
import homeTry.tag.teamTag.model.entity.TeamTag;
import homeTry.tag.teamTag.repository.TeamTagRepository;
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
    private final MemberService memberService;

    private static final String GENDER = "성별";
    private static final String AGE = "나이";
    private static final String EXERCISE_INTENSITY = "운동강도";

    public TeamTagService(TeamTagRepository teamTagRepository, TeamTagMappingService teamTagMappingService, MemberService memberService) {
        this.teamTagRepository = teamTagRepository;
        this.teamTagMappingService = teamTagMappingService;
        this.memberService = memberService;
    }

    //모든 태그 반환
    @Transactional(readOnly = true)
    public AllTeamTagDTO getAllTeamTagList() {
        List<TeamTag> teamTags = teamTagRepository.findAllByIsDeprecatedFalse();

        List<TeamTagDTO> genderTagList = teamTagFilter(teamTags, GENDER);
        List<TeamTagDTO> ageTagList = teamTagFilter(teamTags, AGE);
        List<TeamTagDTO> exerciseIntensityTagList = teamTagFilter(teamTags, EXERCISE_INTENSITY);

        return new AllTeamTagDTO(genderTagList, ageTagList, exerciseIntensityTagList);
    }

    private List<TeamTagDTO> teamTagFilter(List<TeamTag> teamTagList, String tagAttribute) {
        return teamTagList.stream()
                .filter(teamTag -> teamTag.getTagAttribute().value().equals(tagAttribute))
                .map(TeamTagDTO::from)
                .toList();
    }

    //팀이 가지고 있는 tag를 찾아서 tagDTO로 반환
    @Transactional(readOnly = true)
    public List<TeamTagDTO> getTeamTagsOfTeam(Team team) {
        List<TeamTagMapping> teamTagMappingList = teamTagMappingService.getTeamTagMappingsOfTeam(team);
        return teamTagMappingList
                .stream()
                .map(teamTagMapping -> TeamTagDTO.from(teamTagMapping.getTeamTag()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TeamTag> getTeamTagList(List<Long> tagIdList) {
        return tagIdList
                .stream()
                .map(tagId -> teamTagRepository.findById(tagId)
                        .orElseThrow(() -> new TeamTagNotFoundException()))
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamTagResponse getTeamTagResponse(MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        List<TeamTagDTO> teamTagList = teamTagRepository.findAllByIsDeprecatedFalse()
                .stream()
                .map(TeamTagDTO::from)
                .toList();

        return new TeamTagResponse(teamTagList);
    }

    @Transactional
    public void addTeamTag(TeamTagRequest teamTagRequest, MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        if (teamTagRepository.existsByTagName(new TagName(teamTagRequest.teamTagName()))) {
            throw new TeamTagAlreadyExistsException();
        }

        teamTagRepository.save(
                new TeamTag(
                        teamTagRequest.teamTagName(),
                        teamTagRequest.teamTagAttribute())
        );
    }

    @Transactional
    public void deleteTeamTag(Long teamTagId, MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        TeamTag teamTag = teamTagRepository.findById(teamTagId)
                .orElseThrow(() -> new TeamTagNotFoundException());

        teamTag.markAsDeprecated();
    }

    private void verifyAdmin(MemberDTO memberDTO) {
        if (!memberService.isAdmin(memberDTO.id())) {
            throw new ForbiddenTagAccessException();
        }
    }

}
