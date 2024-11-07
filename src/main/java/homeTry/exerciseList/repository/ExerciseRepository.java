package homeTry.exerciseList.repository;

import homeTry.exerciseList.model.entity.Exercise;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
