package homeTry.chatting.service;

import homeTry.chatting.dto.request.ChattingMessageRequest;
import homeTry.chatting.dto.response.ChattingMessageResponse;
import homeTry.chatting.exception.badRequestException.InvalidTeamIdException;
import homeTry.chatting.repository.ChattingRepository;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.service.MemberService;
import homeTry.team.exception.TeamMemberNotFoundException;
import homeTry.team.model.entity.Team;
import homeTry.team.model.entity.TeamMemberMapping;
import homeTry.team.service.TeamMemberMappingService;
import homeTry.team.service.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ChattingService {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamMemberMappingService teamMemberMappingService;
    private final ChattingRepository chattingRepository;


    public ChattingService(TeamService teamService,
            ChattingRepository chattingRepository,
            MemberService memberService, TeamMemberMappingService teamMemberMappingService) {
        this.teamService = teamService;
        this.chattingRepository = chattingRepository;
        this.memberService = memberService;
        this.teamMemberMappingService = teamMemberMappingService;
    }

    @Transactional
    public ChattingMessageResponse saveChattingMessage(Long teamId,
            ChattingMessageRequest chattingMessageRequest,
            MemberDTO memberDTO) {

        Team team = teamService.getTeamEntity(teamId);
        Member member = memberService.getMemberEntity(memberDTO.id());
        TeamMemberMapping teamMemberMapping;

        try {
            teamMemberMapping = teamMemberMappingService.getTeamMemberMapping(team, member);
        } catch (TeamMemberNotFoundException e) {
            throw new InvalidTeamIdException();
        }

        return ChattingMessageResponse.from(
                chattingRepository.save(chattingMessageRequest.toEntity(teamMemberMapping)));
    }

    @Transactional(readOnly = true)
    public Slice<ChattingMessageResponse> getChattingMessageSlice(Long teamId,
            MemberDTO memberDTO, Pageable pageable) {

        Team team = teamService.getTeamEntity(teamId);
        Member member = memberService.getMemberEntity(memberDTO.id());

        //잘못된 teamId 방지
        try {
            teamMemberMappingService.getTeamMemberMapping(team, member);
        } catch (TeamMemberNotFoundException e) {
            throw new InvalidTeamIdException();
        }

        return chattingRepository.findByTeamMemberMappingTeam(team, pageable)
                .map(ChattingMessageResponse::from);
    }


}
