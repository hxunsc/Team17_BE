package homeTry.chatting.service;

import homeTry.chatting.dto.request.ChattingMessageRequest;
import homeTry.chatting.dto.response.ChattingMessageResponse;
import homeTry.chatting.exception.badRequestException.InvalidTeamIdException;
import homeTry.chatting.model.entity.Chatting;
import homeTry.chatting.repository.ChattingRepository;
import homeTry.member.dto.MemberDTO;
import homeTry.member.service.MemberService;
import homeTry.team.model.entity.Team;
import homeTry.team.model.entity.TeamMember;
import homeTry.team.service.TeamMemberService;
import homeTry.team.service.TeamService;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ChattingService {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    private final ChattingRepository chattingRepository;

    public ChattingService(MemberService memberService, TeamService teamService,
            TeamMemberService teamMemberService, ChattingRepository chattingRepository) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.teamMemberService = teamMemberService;
        this.chattingRepository = chattingRepository;
    }

    @Transactional
    public void saveChattingMessage(Long teamId, ChattingMessageRequest chattingMessageRequest,
            MemberDTO memberDTO) {
        Team team = teamService.getTeamEntity(teamId);
        List<TeamMember> teamMembers = teamMemberService.getTeamMember(team);

        System.out.println("teamMembers = " + teamMembers);

        // 해당 멤버 ID와 일치하는 TeamMember 객체 찾기
        TeamMember teamMember = teamMembers.stream()
                .filter(tm -> tm.getMember().getId().equals(memberDTO.id()))
                .findFirst()
                .orElseThrow(InvalidTeamIdException::new);

        chattingRepository.save(chattingMessageRequest.toEntity(teamMember));
    }

    @Transactional(readOnly = true)
    public Slice<ChattingMessageResponse> getChattingMessageSlice(Long teamId, MemberDTO memberDTO) {
        Team team = teamService.getTeamEntity(teamId);

        //잘못된 teamId 방지
        //todo : getTeamMember(team, member) 구현하기
        teamMemberService.getTeamMember(team).stream()
                .filter(tm -> tm.getMember().getId().equals(memberDTO.id()))
                .findFirst()
                .orElseThrow(InvalidTeamIdException::new);

        Slice<Chatting> chattingMessageSlice = chattingRepository.findByTeamMemberTeam(team);

        List<ChattingMessageResponse> chattingMessageResponseList = chattingMessageSlice.getContent()
                .stream()
                .map(ChattingMessageResponse::from)
                .toList();

        return new SliceImpl<>(chattingMessageResponseList, chattingMessageSlice.getPageable(),
                chattingMessageSlice.hasNext());

    }


}
