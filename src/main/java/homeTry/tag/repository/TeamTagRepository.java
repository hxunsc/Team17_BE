package homeTry.tag.repository;

import homeTry.tag.model.entity.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamTagRepository extends JpaRepository<TeamTag, Long> {
}
