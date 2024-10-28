package homeTry.team.dto;

import java.time.Duration;

public record RankingDTO(
        String name,
        int ranking,
        Duration totalExerciseTime
) {
    public static RankingDTO of(String name, int ranking, Duration totalExerciseTime) {
        return new RankingDTO(name, ranking, totalExerciseTime);
    }
}
