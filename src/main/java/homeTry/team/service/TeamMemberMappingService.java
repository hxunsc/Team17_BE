package homeTry.team.service;

import homeTry.member.model.entity.Member;
import homeTry.team.exception.badRequestException.AlreadyJoinedTeamException;
import homeTry.team.exception.badRequestException.TeamMemberNotFoundException;
import homeTry.team.model.entity.Team;
import homeTry.team.model.entity.TeamMemberMapping;
import homeTry.team.repository.TeamMemberMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberMappingService {

    private final TeamMemberMappingRepository teamMemberMappingRepository;

    public TeamMemberMappingService(TeamMemberMappingRepository teamMemberMappingRepository) {
        this.teamMemberMappingRepository = teamMemberMappingRepository;
    }

    //TeamMemberMapping 엔티티 추가 (멤버가 팀 가입시 사용)
    public void addTeamMember(Team team, Member member) {
        teamMemberMappingRepository.findByTeamAndMemberAndActivated(team, member)
                .ifPresent(teamMemberMapping -> {
                    if (!teamMemberMapping.isDeprecated()) // isDeprecated 값이 false인 경우 예외 던짐
                        throw new AlreadyJoinedTeamException();
                    teamMemberMapping.markAsActivated(); // isDeprecated 값이 true인 경우 다시 활성화
                    return;
                });

        teamMemberMappingRepository.save(new TeamMemberMapping(member, team));
    }

    //TeamMemberMapping 엔티티 삭제 (멤버가 팀에서 나갈 시)
    public void markDeprecated(Team team, Member member) {
        TeamMemberMapping teamMemberMapping = teamMemberMappingRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMemberNotFoundException::new);

        teamMemberMapping.markAsDeprecated(); // softDelete 적용
    }

    //팀에 속한 멤버들의 TeamMemberMapping 을 모두 삭제
    public void deleteAllTeamMemberFromTeam(Team team) { //특정 팀에 대한 모든 TeamMember 삭제 (팀 삭제 시)
        teamMemberMappingRepository.deleteByTeam(team);
    }

    //팀에 속한 멤버들을 반환
    public List<Member> getMemberListByTeam(Team team) {
        return teamMemberMappingRepository.findByTeam(team)
                .stream()
                .map(TeamMemberMapping::getMember)
                .toList();
    }

    //특정 TeamMemberMapping 을 반환
    public TeamMemberMapping getTeamMemberMapping(Team team, Member member) {
        return teamMemberMappingRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMemberNotFoundException::new);
    }

    //특정 TeamMemberMapping 을 id를 가지고 반환
    public TeamMemberMapping getTeamMemberMappingById(Long teamId, Long memberId) {
        return teamMemberMappingRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(TeamMemberNotFoundException::new);
    }

    //유저가 가입한 팀 리스트를 반환
    public List<Team> getTeamListByMember(Member member) {
        return teamMemberMappingRepository.findByMember(member)
                .stream()
                .map(TeamMemberMapping::getTeam)
                .toList();
    }
}
