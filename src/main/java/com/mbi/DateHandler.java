package com.mbi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by mbi on 10/27/16.
 */
public class DateHandler {

    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    private static DateTime todayDate = new DateTime(DateTimeZone.UTC);
    private static DateTime todayDateTime = new DateTime(DateTimeZone.UTC);

    public static String todayPlus(int plusDays) {
        return dateFormatter.print(todayDate.plusDays(plusDays));
    }

    public static String plusDaysToDate(String date, int plusDays) {
        DateTime dateTime = dateFormatter.parseDateTime(date);

        return dateFormatter.print(dateTime.plusDays(plusDays));
    }

    public static String minusDaysToDate(String date, int minusDays) {
        DateTime dateTime = dateFormatter.parseDateTime(date);

        return dateFormatter.print(dateTime.minusDays(minusDays));
    }

    public static String todayMinus(int minusDays) {
        return dateFormatter.print(todayDate.minusDays(minusDays));
    }

    public static String todayMinus(int minusDays, int minusMonths) {
        return dateFormatter.print(todayDate.minusDays(minusDays).minusMonths(minusMonths));
    }

    public static String getTodayDate() {
        return dateFormatter.print(todayDate);
    }

    public static String getTodayDateTime() {
        return dateTimeFormatter.print(todayDateTime);
    }

    public static String getDayOfWeek(String dt) {
        DateTime dateTime = dateFormatter.parseDateTime(dt);

        return dateTime.dayOfWeek().getAsText();
    }

    public static int daysBetweenDates(String date1, String date2) {
        DateTime start = dateFormatter.parseDateTime(date1);
        DateTime end = dateFormatter.parseDateTime(date2);

        return Days.daysBetween(start.withTimeAtStartOfDay() , end.withTimeAtStartOfDay() ).getDays();
    }

    public static int getYear() {
        return todayDate.getYear();
    }

    public static int getMonth() {
        return todayDate.getMonthOfYear();
    }

    public static int getDay() {
        return todayDate.getDayOfMonth();
    }

    public static int getHour() {
        return todayDateTime.getHourOfDay();
    }
}
