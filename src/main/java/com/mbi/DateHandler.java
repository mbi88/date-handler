package com.mbi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Different operations with date time.
 */
public final class DateHandler {

    /**
     * Date Pattern.
     */
    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Date time pattern.
     */
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Time zone.
     */
    private final DateTimeZone dateTimeZone;

    /**
     * Constructor with time zone.
     *
     * @param dateTimeZone time zone.
     */
    public DateHandler(final DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    /**
     * Constructor with default time zone UTC.
     */
    public DateHandler() {
        this.dateTimeZone = DateTimeZone.UTC;
    }

    /**
     * Returns current date in format 'yyyy-MM-dd' according to passed time zone.
     *
     * @return current date.
     */
    public String getCurrentDate() {
        final DateTime dateTime = new DateTime(dateTimeZone);
        return dateFormatter.print(dateTime);
    }

    /**
     * Returns current date time in format 'yyyy-MM-dd'T'HH:mm:ss' according to passed time zone.
     *
     * @return current date time.
     */
    public String getCurrentDateTime() {
        final DateTime dateTime = new DateTime(dateTimeZone);
        return dateTimeFormatter.print(dateTime);
    }

    /**
     * Returns current day of week as a text in full format.
     *
     * @return day of week.
     */
    public String getDayOfWeek() {
        return new DateTime(dateTimeZone).dayOfWeek().getAsText();
    }

    /**
     * Returns day of week for passed date as a text in full format.
     *
     * @param date date.
     * @return day of week.
     */
    public String getDayOfWeek(final String date) {
        return dateFormatter.parseDateTime(date).dayOfWeek().getAsText();
    }

    /**
     * Count of days between two dates.
     *
     * @param startDate 1st date.
     * @param endDate   2nd date
     * @return the absolute value of 1st date minus 2nd.
     */
    public int daysBetweenDates(final String startDate, final String endDate) {
        final DateTime start = dateFormatter.parseDateTime(startDate);
        final DateTime end = dateFormatter.parseDateTime(endDate);

        return Math.abs(Days.daysBetween(start.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays());
    }

    /**
     * Adds some period of time to current date. Time period should be in format e.g.: "1y2M3d4h5m6s"
     * Where:
     * 1y - one year.
     * 2M - two months.
     * 3d - three days.
     * 4h - four hours.
     * 5m - 5 minutes.
     * 6s - 6 seconds.
     * Example: current date time = "2017-01-01T12:00:00", formula = "3d5m", method will return "2017-01-04T12:05:00".
     *
     * @param formula period formula.
     * @return updated date time.
     * @throws IllegalArgumentException if formula does not start with digit or does not end with letter or if time
     *                                  period if unknown.
     */
    public String plus(final String formula) {
        final CustomDateTime dt = new DateTimeParser().parse(formula);
        final DateTimeFormatter resultFormatter = isDate(dt) ? dateFormatter : dateTimeFormatter;
        final DateTime dateTime = new DateTime(dateTimeZone);

        return resultFormatter.print(dateTime
                .plusYears(dt.getY())
                .plusMonths(dt.getMo())
                .plusDays(dt.getD())
                .plusHours(dt.getH())
                .plusMinutes(dt.getM())
                .plusSeconds(dt.getS()));
    }

    /**
     * Adds some period of time to passed date. Time period should be in format e.g.: "1y2M3d4h5m6s"
     * Where:
     * 1y - one year.
     * 2M - two months.
     * 3d - three days.
     * 4h - four hours.
     * 5m - 5 minutes.
     * 6s - 6 seconds.
     * Example: passed date time = "2017-01-01T12:00:00", formula = "3d5m", method will return "2017-01-04T12:05:00".
     *
     * @param formula period formula.
     * @param start   date to add time period to.
     * @return updated date time.
     * @throws IllegalArgumentException if formula does not start with digit or does not end with letter or if time
     *                                  period if unknown.
     */
    public String plus(final String start, final String formula) {
        final DateTimeFormatter startFormatter = isDate(start) ? dateFormatter : dateTimeFormatter;
        final DateTime startDateTime = startFormatter.parseDateTime(start);

        final CustomDateTime dt = new DateTimeParser().parse(formula);
        final DateTimeFormatter resultFormatter = (isDate(dt) && isDate(start)) ? dateFormatter : dateTimeFormatter;

        return resultFormatter.print(startDateTime
                .plusYears(dt.getY())
                .plusMonths(dt.getMo())
                .plusDays(dt.getD())
                .plusHours(dt.getH())
                .plusMinutes(dt.getM())
                .plusSeconds(dt.getS()));
    }

    /**
     * Subtracts some period of time from current date. Time period should be in format e.g.: "1y2M3d4h5m6s"
     * Where:
     * 1y - one year.
     * 2M - two months.
     * 3d - three days.
     * 4h - four hours.
     * 5m - 5 minutes.
     * 6s - 6 seconds.
     * Example: current date time = "2017-01-04T12:00:00", formula = "3d5m", method will return "2017-01-01T11:55:00".
     *
     * @param formula period formula.
     * @return updated date time.
     * @throws IllegalArgumentException if formula does not start with digit or does not end with letter or if time
     *                                  period if unknown.
     */
    public String minus(final String formula) {
        final CustomDateTime dt = new DateTimeParser().parse(formula);
        final DateTimeFormatter resultFormatter = isDate(dt) ? dateFormatter : dateTimeFormatter;
        final DateTime dateTime = new DateTime(dateTimeZone);

        return resultFormatter.print(dateTime
                .minusYears(dt.getY())
                .minusMonths(dt.getMo())
                .minusDays(dt.getD())
                .minusHours(dt.getH())
                .minusMinutes(dt.getM())
                .minusSeconds(dt.getS()));
    }

    /**
     * Subtracts some period of time from passed date. Time period should be in format e.g.: "1y2M3d4h5m6s"
     * Where:
     * 1y - one year.
     * 2M - two months.
     * 3d - three days.
     * 4h - four hours.
     * 5m - 5 minutes.
     * 6s - 6 seconds.
     * Example: passed date time = "2017-01-04T12:00:00", formula = "3d5m", method will return "2017-01-01T11:55:00".
     *
     * @param formula period formula.
     * @param start   date to subtract time period from.
     * @return updated date time.
     * @throws IllegalArgumentException if formula does not start with digit or does not end with letter or if time
     *                                  period if unknown.
     */
    public String minus(final String start, final String formula) {
        final DateTimeFormatter startFormatter = isDate(start) ? dateFormatter : dateTimeFormatter;
        final DateTime startDateTime = startFormatter.parseDateTime(start);

        final CustomDateTime dt = new DateTimeParser().parse(formula);
        final DateTimeFormatter resultFormatter = (isDate(dt) && isDate(start)) ? dateFormatter : dateTimeFormatter;

        return resultFormatter.print(startDateTime
                .minusYears(dt.getY())
                .minusMonths(dt.getMo())
                .minusDays(dt.getD())
                .minusHours(dt.getH())
                .minusMinutes(dt.getM())
                .minusSeconds(dt.getS()));
    }

    /**
     * If passed date format matches expected.
     *
     * @param date date format for check.
     * @return result of check.
     */
    private boolean isDate(final String date) {
        return date.matches("^[0-9]{4}(-[0-9]{2}){2}$");
    }

    /**
     * If passed date time object contains hours or minutes or seconds.
     *
     * @param dt date time.
     * @return result of check.
     */
    private boolean isDate(final CustomDateTime dt) {
        return dt.getH() + dt.getM() + dt.getS() == 0;
    }
}
