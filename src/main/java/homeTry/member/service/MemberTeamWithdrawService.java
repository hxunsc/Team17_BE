package homeTry.member.service;

import homeTry.member.model.entity.Member;
import homeTry.team.model.entity.Team;
import homeTry.team.service.TeamMemberMappingService;
import homeTry.team.service.TeamWithdrawService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberTeamWithdrawService {

    private final TeamMemberMappingService teamMemberMappingService;
    private final TeamWithdrawService teamWithdrawService;
    private final MemberService memberService;

    public MemberTeamWithdrawService(TeamMemberMappingService teamMemberMappingService,
            TeamWithdrawService teamWithdrawService, MemberService memberService) {
        this.teamMemberMappingService = teamMemberMappingService;
        this.teamWithdrawService = teamWithdrawService;
        this.memberService = memberService;
    }

    @Transactional
    public void withdrawTeamByWithdrawMember(Long withdrawMemberId) {
        Member withdrawMember = memberService.getMemberEntity(withdrawMemberId);

        List<Team> withdrawTeamList = teamMemberMappingService.getTeamListByMember(withdrawMember);

        for (Team team : withdrawTeamList) {
            if(team.getLeaderId().equals(withdrawMemberId)) {
                teamWithdrawService.deleteTeam(withdrawMemberId, team.getId());
                continue;
            }
            teamWithdrawService.withdrawTeam(withdrawMemberId, team.getId());
        }
    }

}
