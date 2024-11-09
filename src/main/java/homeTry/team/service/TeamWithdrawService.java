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

        //chattingService.deleteTeamChattingMessageAll(team)

        teamService.deleteTeam(memberId, team);
    }

    //팀 탈퇴 기능
    @Transactional
    public void withdrawTeam(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        teamService.withdrawTeam(memberId, team);
    }

    //회원 탈퇴로 인한 팀 삭제 처리 및 팀 탈퇴 처리
    public void withdrawAllTeamsByMemberWithdraw(Long memberId) {
        List<Team> teamList = teamService.getTeamListByLeaderId(memberId);

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
