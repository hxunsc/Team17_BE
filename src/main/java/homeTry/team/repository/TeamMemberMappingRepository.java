package homeTry.team.repository;

import homeTry.member.model.entity.Member;
import homeTry.team.model.entity.Team;
import homeTry.team.model.entity.TeamMemberMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberMappingRepository extends JpaRepository<TeamMemberMapping, Long> {

    Optional<TeamMemberMapping> findByTeamAndMember(Team team, Member member);

    void deleteByTeam(Team team); //특정 팀에 속한 모든 엔티티 삭제

    List<TeamMemberMapping> findByTeam(Team team);

    Slice<TeamMemberMapping> findByMember(Member member, Pageable pageable);
}
