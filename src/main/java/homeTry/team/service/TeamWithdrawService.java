package homeTry.team.service;

import homeTry.chatting.service.ChattingService;
import homeTry.member.model.entity.Member;
import homeTry.member.service.MemberService;
import homeTry.team.exception.NotTeamLeaderException;
import homeTry.team.exception.TeamLeaderCannotWithdrawException;
import homeTry.team.exception.TeamNotFoundException;
import homeTry.team.model.entity.Team;
import homeTry.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamWithdrawService {
    private final TeamService teamService;
    private final ChattingService chattingService;
    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final TeamMemberMappingService teamMemberMappingService;
    private final TeamTagMappingService teamTagMappingService;

    public TeamWithdrawService(
            TeamService teamService,
            ChattingService chattingService,
            TeamRepository teamRepository,
            MemberService memberService,
            TeamMemberMappingService teamMemberMappingService,
            TeamTagMappingService teamTagMappingService) {
        this.teamService = teamService;
        this.chattingService = chattingService;
        this.teamRepository = teamRepository;
        this.memberService = memberService;
        this.teamMemberMappingService = teamMemberMappingService;
        this.teamTagMappingService = teamTagMappingService;
    }

    //팀 삭제 기능
    @Transactional
    public void deleteTeam(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);


        Member member = memberService.getMemberEntity(memberId);

        if (!team.validateIsLeader(memberId)) //팀 리더인지 체크
            throw new NotTeamLeaderException();

//        chattingService.deleteTeamChattingMessageAll(team);

        teamMemberMappingService.deleteAllTeamMemberFromTeam(team); // 해당 팀에 대한 TeamMemberMapping 데이터 삭제

        teamTagMappingService.deleteAllTeamTagMappingFromTeam(team); //해당 팀에 대한 TeamTagMapping 데이터 삭제

        teamRepository.delete(team); //Team 삭제
    }

    //팀 탈퇴 기능
    @Transactional
    public void withdrawTeam(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        Member member = memberService.getMemberEntity(memberId);

        if (team.validateIsLeader(memberId)) { // 팀 리더가 탈퇴하는 요청인지 체크. 팀 리더는 팀 삭제만 가능. 탈퇴는 불가
            throw new TeamLeaderCannotWithdrawException();
        }

        teamMemberMappingService.markDeprecated(team, member); //TeamMemberMapping 테이블에서 softDelete

        team.decreaseParticipantsByWithdraw(); //팀의 현재 참여인원 감소
    }

    //회원 탈퇴로 인한 팀 삭제 처리 및 팀 탈퇴 처리
    public void withdrawAllTeamsByMemberWithdraw(Long memberId) {
        List<Team> teamList = teamRepository.findByLeaderId(memberId);

        deleteTeamByTeamLeaderWithdraw(teamList, memberId);

        withDrawAllTeams(teamList, memberId);
    }

    //회원탈퇴로 인한 해당 회원이 팀의 멤버로 속해있는 TeamMemberMapping에 대해 softDelete 적용
    private void withDrawAllTeams(List<Team> teamList, Long memberId) {
        teamList.forEach(
                team -> withdrawTeam(memberId, team.getId())
        );
    }

    //회원탈퇴로 인해 해당 회원이 팀 리더인 팀 일괄 삭제
    private void deleteTeamByTeamLeaderWithdraw(List<Team> teamList, Long memberId) {
        teamList.forEach(
                team -> deleteTeam(memberId, team.getId())
        );
    }
}
