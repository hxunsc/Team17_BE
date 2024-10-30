package homeTry.adminTag.teamTag.repository;

import homeTry.adminTag.teamTag.model.entity.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamTagRepository extends JpaRepository<TeamTag, Long> {
}
