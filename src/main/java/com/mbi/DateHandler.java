package com.mbi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.DayOfWeek;
import java.util.Locale;

import static org.testng.Assert.assertTrue;

/**
 * Different operations with date time.
 */
public final class DateHandler {

    /**
     * Invalid date time format error message.
     */
    private static final String INVALID_DATE_FORMAT_ERROR_MESSAGE = "Incorrect date format";

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
        return dateFormatter.print(new DateTime(dateTimeZone));
    }

    /**
     * Returns current date time in format 'yyyy-MM-dd'T'HH:mm:ss' according to passed time zone.
     *
     * @return current date time.
     */
    public String getCurrentDateTime() {
        return dateTimeFormatter.print(new DateTime(dateTimeZone));
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
        final var start = dateFormatter.parseDateTime(startDate);
        final var end = dateFormatter.parseDateTime(endDate);

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
        return plus(dateFormatter.print(DateTime.now(dateTimeZone)), formula);
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
        final var formulaDateTime = new DateTimeParser().parse(formula);
        final var startFormatter = isDate(start) ? dateFormatter : dateTimeFormatter;
        final var startDateTime = startFormatter.parseDateTime(start);
        final var resultFormatter = (isDate(formulaDateTime) && isDate(start)) ? dateFormatter : dateTimeFormatter;

        return resultFormatter.print(applyOffset(startDateTime, formulaDateTime, true));
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
        return minus(dateFormatter.print(DateTime.now(dateTimeZone)), formula);
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
        final var formulaDateTime = new DateTimeParser().parse(formula);
        final var startFormatter = isDate(start) ? dateFormatter : dateTimeFormatter;
        final var startDateTime = startFormatter.parseDateTime(start);
        final var resultFormatter = (isDate(formulaDateTime) && isDate(start)) ? dateFormatter : dateTimeFormatter;

        return resultFormatter.print(applyOffset(startDateTime, formulaDateTime, false));
    }

    /**
     * Returns current year.
     *
     * @return current year.
     */
    public int getYear() {
        return new DateTime(dateTimeZone).getYear();
    }

    /**
     * Returns year from date.
     *
     * @param date date.
     * @return year.
     * @throws AssertionError if date format is incorrect.
     */
    public int getYear(final String date) {
        return parse(date).getYear();
    }

    /**
     * Returns current month.
     *
     * @return current month.
     */
    public int getMonth() {
        return new DateTime(dateTimeZone).getMonthOfYear();
    }

    /**
     * Returns month from date.
     *
     * @param date date.
     * @return month.
     * @throws AssertionError if date format is incorrect.
     */
    public int getMonth(final String date) {
        return parse(date).getMonthOfYear();
    }

    /**
     * Returns current day.
     *
     * @return current day.
     */
    public int getDay() {
        return new DateTime(dateTimeZone).getDayOfMonth();
    }

    /**
     * Returns day from date.
     *
     * @param date date.
     * @return day.
     * @throws AssertionError if date format is incorrect.
     */
    public int getDay(final String date) {
        return parse(date).getDayOfMonth();
    }

    /**
     * Returns current hour.
     *
     * @return current hour.
     */
    public int getHour() {
        return new DateTime(dateTimeZone).getHourOfDay();
    }

    /**
     * Returns hour from date.
     *
     * @param date date.
     * @return hour.
     * @throws AssertionError if date format is incorrect.
     */
    public int getHour(final String date) {
        return parse(date).getHourOfDay();
    }

    /**
     * Returns current minute.
     *
     * @return current minute.
     */
    public int getMinute() {
        return new DateTime(dateTimeZone).getMinuteOfHour();
    }

    /**
     * Returns minute from date.
     *
     * @param date date.
     * @return minute.
     * @throws AssertionError if date format is incorrect.
     */
    public int getMinute(final String date) {
        return parse(date).getMinuteOfHour();
    }

    /**
     * Returns current second.
     *
     * @return current second.
     */
    public int getSecond() {
        return new DateTime(dateTimeZone).getSecondOfMinute();
    }

    /**
     * Returns second from date.
     *
     * @param date date.
     * @return second.
     * @throws AssertionError if date format is incorrect.
     */
    public int getSecond(final String date) {
        return parse(date).getSecondOfMinute();
    }

    /**
     * Returns the start of the week (Monday) for the given date.
     *
     * @param date the reference date (format yyyy-MM-dd)
     * @return the start of the week (Monday) in the same ISO week as the input date
     */
    public String getStartOfWeek(final String date) {
        return getDayInWeek(date, "Monday");
    }

    /**
     * Returns the end of the week (Sunday) for the given date.
     *
     * @param date the reference date (format yyyy-MM-dd)
     * @return the end of the week (Sunday) in the same ISO week as the input date
     */
    public String getEndOfWeek(final String date) {
        return getDayInWeek(date, "Sunday");
    }

    /**
     * Returns the date of the specified day of the week in the same ISO week as the given date.
     * <p>
     * If the target day comes after the day of the given date, the result will be a future date.
     * If it comes before, the result will be a past date. The result is always within the same ISO week
     * (Monday to Sunday) as the input date.
     *
     * @param date      the reference date (format yyyy-MM-dd)
     * @param targetDay the name of the day of the week (e.g., "Monday", "Saturday")
     * @return the date of that day in the same ISO week as the input date
     */
    public String getDayInWeek(final String date, final String targetDay) {
        final var inputDate = parse(date);
        final var currentDay = inputDate.getDayOfWeek();
        final var target = DayOfWeek.valueOf(targetDay.toUpperCase(Locale.getDefault()));

        final int diff = target.getValue() - currentDay; // e.g. Saturday(6) - Thursday(4) = +2

        return dateFormatter.print(inputDate.plusDays(diff));
    }

    /**
     * If passed date format matches expected.
     *
     * @param date date format for check.
     * @return result of check.
     */
    private boolean isDate(final String date) {
        return date.matches("^\\d{4}(-\\d{2}){2}$");
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

    /**
     * If passed date time format matches expected.
     *
     * @param dateTime date time format for check.
     * @return result of check.
     */
    private boolean isDateTime(final String dateTime) {
        return dateTime.matches("^\\d{4}(-\\d{2}){2}T(\\d{2}:){2}\\d{2}$");
    }

    /**
     * Parses a date or datetime string into a Joda-Time {@link DateTime} object.
     * <p>
     * The method automatically detects the format (date or datetime) and throws an
     * assertion error if the input does not match any of the supported formats.
     *
     * @param date the date or datetime string to parse, must be in format yyyy-MM-dd or yyyy-MM-dd'T'HH:mm:ss
     * @return parsed {@link DateTime} object
     * @throws AssertionError if the format is invalid
     */
    private DateTime parse(final String date) {
        assertTrue(isDate(date) || isDateTime(date), INVALID_DATE_FORMAT_ERROR_MESSAGE);
        final var formatter = isDate(date) ? dateFormatter : dateTimeFormatter;
        return formatter.parseDateTime(date);
    }

    /**
     * Applies a custom date/time offset to the given base {@link DateTime}.
     * <p>
     * If {@code add} is true, the offset will be added; otherwise, it will be subtracted.
     *
     * @param base   the base {@link DateTime} to apply the offset to
     * @param offset the {@link CustomDateTime} object containing the offset values
     * @param add    true to add the offset, false to subtract it
     * @return updated {@link DateTime} with the offset applied
     */
    private DateTime applyOffset(final DateTime base, final CustomDateTime offset, final boolean add) {
        return (add ? base.plusYears(offset.getY()) : base.minusYears(offset.getY()))
                .plusMonths(add ? offset.getMo() : -offset.getMo())
                .plusDays(add ? offset.getD() : -offset.getD())
                .plusHours(add ? offset.getH() : -offset.getH())
                .plusMinutes(add ? offset.getM() : -offset.getM())
                .plusSeconds(add ? offset.getS() : -offset.getS());
    }
}
