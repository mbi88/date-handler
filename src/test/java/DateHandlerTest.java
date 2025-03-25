import com.mbi.DateHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.annotations.Test;

import java.util.TimeZone;

import static org.testng.Assert.*;

public class DateHandlerTest {

    private final DateHandler date = new DateHandler();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final DateTimeZone kyivZone = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

    @Test
    public void testDefaultConstructor() {
        var date = new DateHandler();
        var now = DateTime.now(DateTimeZone.UTC);

        assertEquals(date.getCurrentDateTime(), dateTimeFormatter.print(now));
    }

    @Test
    public void testConstructorWithTimeZone() {
        var date = new DateHandler(kyivZone);
        var now = DateTime.now(kyivZone);

        assertEquals(date.getCurrentDateTime(), dateTimeFormatter.print(now));
    }

    @Test
    public void testGetCurrentDate() {
        var now = DateTime.now(DateTimeZone.UTC);

        assertEquals(date.getCurrentDate(), dateFormatter.print(now));
    }

    @Test
    public void testCurrentDateTime() {
        var now = DateTime.now(DateTimeZone.UTC);

        assertEquals(date.getCurrentDateTime(), dateTimeFormatter.print(now));
    }

    @Test
    public void testCurrentDateTimeInCustomZone() {
        var now = DateTime.now(kyivZone);
        var dh = new DateHandler(kyivZone);

        assertEquals(dh.getCurrentDateTime(), dateTimeFormatter.print(now));
    }

    @Test
    public void testDateTimeIsCurrent() throws InterruptedException {
        Thread.sleep(3000);
        var dateHandler = new DateHandler();

        assertEquals(dateHandler.getCurrentDateTime(), date.getCurrentDateTime());
    }

    @Test
    public void testDayOfWeek() {
        assertEquals(date.getDayOfWeek(), DateTime.now(DateTimeZone.UTC).dayOfWeek().getAsText());
        assertEquals(date.getDayOfWeek("2018-03-20"), "Tuesday");
    }

    @Test
    public void testDaysBetweenDates() {
        assertEquals(date.daysBetweenDates("2018-03-20", "2018-03-20"), 0);
        assertEquals(date.daysBetweenDates("2018-03-21", "2018-03-22"), 1);
        assertEquals(date.daysBetweenDates("2018-03-21", "2018-03-20"), 1);
    }

    @Test
    public void testPlus() {
        var dateHandler = new DateHandler(kyivZone);
        var currentDt = dateHandler.getCurrentDateTime();
        var newDt = dateHandler.plus(currentDt, "2y2m1s");

        assertEquals(DateTime.parse(newDt).getYear(), DateTime.parse(currentDt).getYear() + 2);
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear());
        assertEquals(DateTime.parse(newDt).getDayOfMonth(), DateTime.parse(currentDt).getDayOfMonth());
        assertEquals(DateTime.parse(newDt).getMinuteOfHour(), DateTime.parse(currentDt).getMinuteOfHour() + 2,
                "Ignore if expected = 61 but actual = 1");
        assertEquals(DateTime.parse(newDt).getHourOfDay(), DateTime.parse(currentDt).getHourOfDay());
        assertEquals(DateTime.parse(newDt).getSecondOfMinute(), DateTime.parse(currentDt).getSecondOfMinute() + 1);

        int expectedMonth = (DateTime.parse(newDt).getMonthOfYear() > 10)
                ? DateTime.parse(newDt).getMonthOfYear() - 12
                : DateTime.parse(newDt).getMonthOfYear();
        newDt = dateHandler.plus("2M");
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), expectedMonth + 2);

        assertEquals(date.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");

        assertEquals(dateHandler.plus("2017-01-01", "2d2h"), "2017-01-03T02:00:00");
        assertEquals(dateHandler.plus("2017-01-01", "2d"), "2017-01-03");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d"), "2017-01-03T01:00:00");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");
    }

    @Test
    public void testPlusMonth() {
        var dateHandler = new DateHandler(kyivZone);
        var currentDt = dateHandler.getCurrentDateTime();
        String newDt;

        for (int plusMonth = 0; plusMonth < 50; plusMonth++) {
            newDt = dateHandler.plus(plusMonth + "M");
            int compensator = 12 * ((DateTime.parse(currentDt).getMonthOfYear() + plusMonth) / 12);
            int expectedMonth = ((DateTime.parse(currentDt).getMonthOfYear() + plusMonth) > 12
                    ? DateTime.parse(currentDt).getMonthOfYear() + plusMonth - compensator
                    : DateTime.parse(currentDt).getMonthOfYear() + plusMonth);
            expectedMonth = expectedMonth == 0 ? 12 : expectedMonth;

            assertEquals(DateTime.parse(newDt).getMonthOfYear(), expectedMonth);
        }
    }

    @Test
    public void testFormulaFormat() {
        assertThrows(IllegalArgumentException.class, () -> date.plus("21"));
        assertThrows(IllegalArgumentException.class, () -> date.plus("d21"));
        assertThrows(IllegalArgumentException.class, () -> date.plus("21dasds"));
    }

    @Test
    public void testMinus() {
        var dateHandler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
        var currentDt = dateHandler.getCurrentDateTime();
        var newDt = dateHandler.minus(currentDt, "2y2m1s");

        assertEquals(DateTime.parse(newDt).getYear(), DateTime.parse(currentDt).getYear() - 2);
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear());
        assertEquals(DateTime.parse(newDt).getDayOfMonth(), DateTime.parse(currentDt).getDayOfMonth());
        assertEquals(DateTime.parse(newDt).getMinuteOfHour(), DateTime.parse(currentDt).getMinuteOfHour() - 2);
        assertEquals(DateTime.parse(newDt).getHourOfDay(), DateTime.parse(currentDt).getHourOfDay());
        assertEquals(DateTime.parse(newDt).getSecondOfMinute(),
                DateTime.parse(currentDt).getSecondOfMinute() - 1);

        assertEquals(date.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");

        assertEquals(dateHandler.minus("2017-01-01", "2d2h"), "2016-12-29T22:00:00");
        assertEquals(dateHandler.minus("2017-01-01", "2d"), "2016-12-30");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d"), "2016-12-30T01:00:00");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");
    }

    @Test
    public void testMinusMonth() {
        var dateHandler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
        var currentDt = dateHandler.getCurrentDateTime();
        String newDt;

        for (int minusMonth = 0; minusMonth < 50; minusMonth++) {
            newDt = dateHandler.minus(minusMonth + "M");
            int expectedMonth = DateTime.parse(currentDt).getMonthOfYear() - (minusMonth % 12);
            if (expectedMonth == 0) {
                expectedMonth = 12;
            } else if (expectedMonth < 0) {
                expectedMonth = (DateTime.parse(currentDt).getMonthOfYear() - (minusMonth % 12)) + 12;
            } else {
                expectedMonth = (DateTime.parse(currentDt).getMonthOfYear() - (minusMonth % 12));
            }

            assertEquals(DateTime.parse(newDt).getMonthOfYear(), expectedMonth);
        }
    }

    @Test
    public void testYear() {
        assertEquals(date.getYear(), new DateTime(DateTimeZone.UTC).getYear());
        assertEquals(date.getYear("2017-02-04T12:02:10"), 2017);
        assertEquals(date.getYear("2017-02-04"), 2017);

        assertThrows(AssertionError.class, () -> date.getYear("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getYear("2017-02-04T23"));
    }

    @Test
    public void testMonth() {
        assertEquals(date.getMonth(), new DateTime(DateTimeZone.UTC).getMonthOfYear());
        assertEquals(date.getMonth("2017-02-04T12:02:10"), 2);
        assertEquals(date.getMonth("2017-02-04"), 2);

        assertThrows(AssertionError.class, () -> date.getMonth("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getMonth("2017-02-04T23"));
    }

    @Test
    public void testDay() {
        assertEquals(date.getDay(), new DateTime(DateTimeZone.UTC).getDayOfMonth());
        assertEquals(date.getDay("2017-02-04T12:02:10"), 4);
        assertEquals(date.getDay("2017-02-04"), 4);

        assertThrows(AssertionError.class, () -> date.getDay("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getDay("2017-02-04T23"));
    }

    @Test
    public void testHour() {
        assertEquals(date.getHour(), new DateTime(DateTimeZone.UTC).getHourOfDay());
        assertEquals(date.getHour("2017-02-04T12:02:10"), 12);
        assertEquals(date.getHour("2017-02-04"), 0);

        assertThrows(AssertionError.class, () -> date.getHour("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getHour("2017-02-04T23"));
    }

    @Test
    public void testMinute() {
        assertEquals(date.getMinute(), new DateTime(DateTimeZone.UTC).getMinuteOfHour());
        assertEquals(date.getMinute("2017-02-04T12:02:10"), 2);
        assertEquals(date.getMinute("2017-02-04"), 0);

        assertThrows(AssertionError.class, () -> date.getMinute("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getMinute("2017-02-04T23"));
    }

    @Test
    public void testSecond() {
        assertEquals(date.getSecond(), new DateTime(DateTimeZone.UTC).getSecondOfMinute());
        assertEquals(date.getSecond("2017-02-04T12:02:10"), 10);
        assertEquals(date.getSecond("2017-02-04"), 0);

        assertThrows(AssertionError.class, () -> date.getSecond("2017-02-0"));
        assertThrows(AssertionError.class, () -> date.getSecond("2017-02-04T23"));
        assertThrows(AssertionError.class, () -> date.getSecond(""));
        assertThrows(NullPointerException.class, () -> date.getSecond(null));
    }

    @Test
    public void testCantParseNullFormula() {
        var ex = expectThrows(IllegalArgumentException.class, () -> date.plus(null));
        assertEquals(ex.getMessage(), "Formula must not be null or blank");
    }

    @Test
    public void testCantParseEmptyFormula() {
        var ex = expectThrows(IllegalArgumentException.class, () -> date.plus(""));
        assertEquals(ex.getMessage(), "Formula must not be null or blank");
    }

    @Test
    public void testCantParseUnknownFormulaField() {
        var ex = expectThrows(IllegalArgumentException.class, () -> date.plus("2y2m1dd"));
        assertEquals(ex.getMessage(), """
                Unrecognized field "dd" (class com.mbi.CustomDateTime), not marked as ignorable (6 known properties: "mo", "s", "d", "h", "y", "m"])
                 at [Source: UNKNOWN; byte offset: #UNKNOWN] (through reference chain: com.mbi.CustomDateTime["dd"])""");
    }
}
