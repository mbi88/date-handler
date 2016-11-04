package com.mbi;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by mbi on 10/27/16.
 */
public class DateHandler {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static DateTime now = new DateTime();

    public static String todayPlus(int plusDays) {
        return formatter.print(now.plusDays(plusDays));
    }

    public static String plusDaysToDate(String date, int plusDays) {
        DateTime dateTime = formatter.parseDateTime(date);

        return formatter.print(dateTime.plusDays(plusDays));
    }

    public static String minusDaysToDate(String date, int minusDays) {
        DateTime dateTime = formatter.parseDateTime(date);

        return formatter.print(dateTime.minusDays(minusDays));
    }

    public static String todayMinus(int minusDays) {
        return formatter.print(now.minusDays(minusDays));
    }

    public static String todayMinus(int minusDays, int minusMonths) {
        return formatter.print(now.minusDays(minusDays).minusMonths(minusMonths));
    }

    public static String today() {
        return formatter.print(now);
    }

    public static String getDayOfWeek(String dt) {
        DateTime dateTime = formatter.parseDateTime(dt);

        return dateTime.dayOfWeek().getAsText();
    }

    public static int daysBetweenDates(String date1, String date2) {
        DateTime start = formatter.parseDateTime(date1);
        DateTime end = formatter.parseDateTime(date2);

        return Days.daysBetween(start.withTimeAtStartOfDay() , end.withTimeAtStartOfDay() ).getDays();
    }
}
