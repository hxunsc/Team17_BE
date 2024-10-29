package homeTry.mainPage.dto.request;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;

import homeTry.common.annotation.DateValid;

public record MainPageRequest(
    @DateValid
    @DateTimeFormat(pattern = "yyyyMMdd")
    LocalDate date
){  
    public MainPageRequest(LocalDate date) {
        this.date = Optional.ofNullable(date).orElse(LocalDate.now());
    }

    public boolean isToday(LocalDate date) {
        return date.equals(LocalDate.now());
    }
}
