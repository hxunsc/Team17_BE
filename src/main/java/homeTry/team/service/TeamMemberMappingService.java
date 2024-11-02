package homeTry.team.service;

import homeTry.member.model.entity.Member;
import homeTry.team.exception.AlreadyJoinedTeamException;
import homeTry.team.exception.TeamMemberNotFoundException;
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
        teamMemberMappingRepository.findByTeamAndMember(team, member) // 이미 가입되어 있는경우 예외 던짐
                .ifPresent(teamMemberMapping -> {
                    throw new AlreadyJoinedTeamException();
                });

        teamMemberMappingRepository.save(new TeamMemberMapping(member, team));
    }

    //TeamMemberMapping 엔티티 삭제 (멤버가 팀에서 나갈 시)
    public void deleteTeamMember(Team team, Member member) {
        TeamMemberMapping teamMemberMapping = teamMemberMappingRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMemberNotFoundException::new);

        teamMemberMappingRepository.delete(teamMemberMapping);
    }

    //팀에 속한 멤버들의 TeamMemberMapping 을 모두 삭제
    public void deleteAllTeamMemberFromTeam(Team team) { //특정 팀에 대한 모든 TeamMember 삭제 (팀 삭제 시)
        teamMemberMappingRepository.deleteByTeam(team);
    }

    //팀에 속한 멤버들의 TeamMemberMapping 을 반환
    public List<TeamMemberMapping> getTeamMemberMapping(Team team) {
        return teamMemberMappingRepository.findByTeam(team);
    }

    //특정 TeamMemberMapping 을 반환
    public TeamMemberMapping getTeamMemberMapping(Team team, Member member) {
        return teamMemberMappingRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMemberNotFoundException::new);
    }

}
