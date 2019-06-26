package ru.piom.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class TimeTrackerTest extends FillTimeTracker {

    private int year = Year.now().getValue();
    private int month = 6;
    private int maxNumberDay = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();


    private List<Integer> excludeDates = Arrays.asList(12);
    private List<Integer> holidayDates = Arrays.asList(12);
    private List<Integer> vacationDates = Arrays.asList();

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    public void FillTimeTracker() {
        List<String> days = IntStream
                .rangeClosed(1, maxNumberDay)
                .mapToObj(
                        item -> LocalDate.of(year, month, item))
                .filter(cal -> cal.getDayOfWeek().getValue() < 6 && !excludeDates.contains(cal.getDayOfMonth()))
                .map(date -> date.format(dateFormat) + " 0:00:00")
                .collect(Collectors.toList());

        $(By.id("ctl00__content_calDatesSelector")).find(By.linkText(">>")).click();

        days.forEach(item -> {
            $(By.id("ctl00__content_ddlDays")).selectOptionByValue(item);
            $(By.id("ctl00__content_ddlProjects")).selectOptionByValue("3012");
            $(By.id("ctl00__content_ddlCategories")).selectOption("Development");
            Integer startH = Integer.valueOf($(By.id("ctl00__content_ucStartTime_ddlHour")).val());
            int endH = startH + 8;
            $(By.id("ctl00__content_ucEndTime_ddlHour")).selectOption(Integer.toString(endH));
            $(By.id("ctl00__content_tboxDescription")).val("Development");
            $(By.id("ctl00__content_lbtnAddEntry")).click();
        });

        getDays(holidayDates).forEach(item -> {
            $(By.id("ctl00__content_ddlDays")).selectOptionByValue(item);
            $(By.id("ctl00__content_ddlProjects")).selectOptionByValue("3011");
            $(By.id("ctl00__content_ddlCategories")).selectOption("Holiday");
            Integer startH = Integer.valueOf($(By.id("ctl00__content_ucStartTime_ddlHour")).val());
            int endH = startH + 8;
            $(By.id("ctl00__content_ucEndTime_ddlHour")).selectOption(Integer.toString(endH));
            $(By.id("ctl00__content_tboxDescription")).val("Holiday");
            $(By.id("ctl00__content_lbtnAddEntry")).click();
        });
        getDays(vacationDates).forEach(item -> {
            $(By.id("ctl00__content_ddlDays")).selectOptionByValue(item);
            $(By.id("ctl00__content_ddlProjects")).selectOptionByValue("3011");
            $(By.id("ctl00__content_ddlCategories")).selectOptionByValue("26");
            Integer startH = Integer.valueOf($(By.id("ctl00__content_ucStartTime_ddlHour")).val());
            int endH = startH + 8;
            $(By.id("ctl00__content_ucEndTime_ddlHour")).selectOption(Integer.toString(endH));
            $(By.id("ctl00__content_tboxDescription")).val("Vacation paid");
            $(By.id("ctl00__content_lbtnAddEntry")).click();
        });
    }

    private List<String> getDays(List<Integer> dates) {
        return dates.stream()
                .map(item -> LocalDate.of(year, month, item))
                .filter(cal -> cal.getDayOfWeek().getValue() < 6)
                .map(date -> date.format(dateFormat) + " 0:00:00")
                .collect(Collectors.toList());
    }
}
