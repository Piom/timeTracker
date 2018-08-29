package ru.piom.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class TimeTrackerTest extends FillTimeTracker {

    static int month = 8;

    List<Integer> excludeDates = Arrays.asList(1, 2, 3, 15);

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    public void FillTimeTracker() {
        List<String> days = IntStream
                .rangeClosed(13, 31)
                .mapToObj(
                        item -> LocalDate.of(2018, month, item))
                .filter(cal -> cal.getDayOfWeek().getValue() < 6 && !excludeDates.contains(cal.getDayOfMonth()))
                .map(date -> date.format(dateFormat) + " 0:00:00")
                .collect(Collectors.toList());
        $(By.id("ctl00__content_calDatesSelector")).find(By.linkText(">>")).click();

        days.forEach(item -> {
            $(By.id("ctl00__content_ddlDays")).selectOptionByValue(item);
            $(By.id("ctl00__content_ddlCategories")).selectOption("Development");
            Integer startH = Integer.valueOf($(By.id("ctl00__content_ucStartTime_ddlHour")).val());
            Integer endH = startH + 8;
            $(By.id("ctl00__content_ucEndTime_ddlHour")).selectOption(endH.toString());
            $(By.id("ctl00__content_tboxDescription")).val("Development");
            $(By.id("ctl00__content_lbtnAddEntry")).click();
        });
    }
}
