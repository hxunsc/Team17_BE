package homeTry.exerciseList.model.entity;

import homeTry.common.converter.DurationToLongConverter;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "exercise_history",
    indexes = {
        @Index(name = "idx_exercise_history_exercise_created", columnList = "exercise_id, created_at")
    }
)
public class ExerciseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    @Convert(converter = DurationToLongConverter.class)
    private Duration exerciseHistoryTime;

    // 운동이 실제로 진행된 날짜
    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected ExerciseHistory() {

    }

    public ExerciseHistory(Exercise exercise, Duration exerciseHistoryTime, LocalDateTime createdAt) {
        this.exercise = exercise;
        this.exerciseHistoryTime = exerciseHistoryTime;
        this.createdAt = createdAt;
    }

    public Long getExerciseHistoryId() {
        return id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Duration getExerciseHistoryTime() {
        return exerciseHistoryTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
