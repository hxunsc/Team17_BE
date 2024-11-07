package homeTry.exerciseList.repository;

import homeTry.exerciseList.model.entity.ExerciseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Long> {

    // ExerciseHistory 리스트 조회 - 멤버 ID와 특정 날짜, 유효한(삭제되지 않은) 운동만
    @Query("SELECT eh FROM ExerciseHistory eh " +
        "JOIN FETCH eh.exercise e " +
        "WHERE e.member.id = :memberId " +
        "AND eh.createdAt BETWEEN :startOfDay AND :endOfDay " +
        "AND e.isDeprecated = false")
    List<ExerciseHistory> findValidExerciseHistoriesForMemberOnDate(
        @Param("memberId") Long memberId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);

}
