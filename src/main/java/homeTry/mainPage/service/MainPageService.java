package homeTry.mainPage.service;

import homeTry.diary.service.DiaryService;
import homeTry.exerciseList.service.ExerciseHistoryService;
import homeTry.exerciseList.service.ExerciseTimeService;
import homeTry.mainPage.dto.response.MainPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MainPageService {

    private final DiaryService diaryService;
    private final ExerciseTimeService exerciseTimeService;
    private final ExerciseHistoryService exerciseHistoryService;

    public MainPageService(DiaryService diaryService,
            ExerciseTimeService exerciseTimeService,
            ExerciseHistoryService exerciseHistoryService) {
        this.diaryService = diaryService;
        this.exerciseTimeService = exerciseTimeService;
        this.exerciseHistoryService = exerciseHistoryService;
    }

    @Transactional(readOnly = true)
    public MainPageResponse getMainPage(LocalDate date, Long memberId, Pageable pageable) {

        if (LocalDate.now().isEqual(date)) {
            return getTodayMainPageResponse(memberId, pageable);
        }

        return getHistoricalMainPageResponse(memberId, date, pageable);

    }

    private MainPageResponse getTodayMainPageResponse(Long memberId, Pageable pageable) {

        return new MainPageResponse(
                exerciseTimeService.getExerciseTimesForToday(memberId),
                exerciseTimeService.getExerciseResponsesForToday(memberId),
                diaryService.getDiaryByDate(LocalDate.now(), memberId, pageable));

    }

    private MainPageResponse getHistoricalMainPageResponse(Long memberId, LocalDate date, Pageable pageable) {

        return new MainPageResponse(
                exerciseHistoryService.getExerciseHistoriesForDay(memberId, date),
                exerciseHistoryService.getExerciseResponsesForDay(memberId, date),
                diaryService.getDiaryByDate(date, memberId, pageable));
    }
}
