import com.mbi.DateHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.annotations.Test;

import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class DateHandlerTest {

    private final DateHandler date = new DateHandler();

    @Test
    public void testDefaultConstructor() {
        DateHandler date = new DateHandler();
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTime dt = new DateTime(DateTimeZone.UTC);

        assertEquals(date.getCurrentDateTime(), dateFormatter.print(dt));
    }

    @Test
    public void testConstructorWithTimeZone() {
        DateHandler date = new DateHandler(DateTimeZone.getDefault());
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTime dt = new DateTime(DateTimeZone.getDefault());

        assertEquals(date.getCurrentDateTime(), dateFormatter.print(dt));
    }

    @Test
    public void testCurrentDate() {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = new DateTime(DateTimeZone.UTC);

        assertEquals(date.getCurrentDate(), dateFormatter.print(dt));
    }

    @Test
    public void testCurrentDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTime dt = new DateTime(DateTimeZone.UTC);

        assertEquals(date.getCurrentDateTime(), dateFormatter.print(dt));

        dt = new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
        DateHandler dh = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));

        assertEquals(dh.getCurrentDateTime(), dateFormatter.print(dt));
    }

    @Test
    public void testDateTimeIsCurrent() throws InterruptedException {
        Thread.sleep(3000);
        DateHandler dateHandler = new DateHandler();

        assertEquals(dateHandler.getCurrentDateTime(), date.getCurrentDateTime());
    }

    @Test
    public void testDayOfWeek() {
        assertEquals(date.getDayOfWeek(), new DateTime(DateTimeZone.UTC).dayOfWeek().getAsText());
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
        DateHandler dateHandler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
        String currentDt = dateHandler.getCurrentDateTime();
        String newDt = dateHandler.plus("2y2m1s");

        assertEquals(DateTime.parse(newDt).getYear(), DateTime.parse(currentDt).getYear() + 2);
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear());
        assertEquals(DateTime.parse(newDt).getDayOfMonth(), DateTime.parse(currentDt).getDayOfMonth());
        assertEquals(DateTime.parse(newDt).getMinuteOfHour(), DateTime.parse(currentDt).getMinuteOfHour() + 2,
                "Ignore if expected = 61 but actual = 1");
        assertEquals(DateTime.parse(newDt).getHourOfDay(), DateTime.parse(currentDt).getHourOfDay());
        assertEquals(DateTime.parse(newDt).getSecondOfMinute(),
                DateTime.parse(dateHandler.getCurrentDateTime()).getSecondOfMinute() + 1);

        newDt = dateHandler.plus("2M");
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear() + 2);

        assertEquals(date.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");

        assertEquals(dateHandler.plus("2017-01-01", "2d2h"), "2017-01-03T02:00:00");
        assertEquals(dateHandler.plus("2017-01-01", "2d"), "2017-01-03");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d"), "2017-01-03T01:00:00");
        assertEquals(dateHandler.plus("2017-01-01T01:00:00", "2d2h"), "2017-01-03T03:00:00");
    }

    @Test
    public void testFormulaFormat() {
        boolean passed;
        try {
            date.plus("21");
            passed = true;
        } catch (IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);

        try {
            date.plus("d21");
            passed = true;
        } catch (IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);

        try {
            date.plus("21dasds");
            passed = true;
        } catch (IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    @Test
    public void testMinus() {
        DateHandler dateHandler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
        String currentDt = dateHandler.getCurrentDateTime();
        String newDt = dateHandler.minus("2y2m1s");

        assertEquals(DateTime.parse(newDt).getYear(), DateTime.parse(currentDt).getYear() - 2);
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear());
        assertEquals(DateTime.parse(newDt).getDayOfMonth(), DateTime.parse(currentDt).getDayOfMonth());
        assertEquals(DateTime.parse(newDt).getMinuteOfHour(), DateTime.parse(currentDt).getMinuteOfHour() - 2);
        assertEquals(DateTime.parse(newDt).getHourOfDay(), DateTime.parse(currentDt).getHourOfDay());
        assertEquals(DateTime.parse(newDt).getSecondOfMinute(),
                DateTime.parse(dateHandler.getCurrentDateTime()).getSecondOfMinute() - 1);

        newDt = dateHandler.minus("2M");
        assertEquals(DateTime.parse(newDt).getMonthOfYear(), DateTime.parse(currentDt).getMonthOfYear() - 2);

        assertEquals(date.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");

        assertEquals(dateHandler.minus("2017-01-01", "2d2h"), "2016-12-29T22:00:00");
        assertEquals(dateHandler.minus("2017-01-01", "2d"), "2016-12-30");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d"), "2016-12-30T01:00:00");
        assertEquals(dateHandler.minus("2017-01-01T01:00:00", "2d2h"), "2016-12-29T23:00:00");
    }

    @Test
    public void testCurrentYear() {
        assertEquals(date.getCurrentYear(), new DateTime(DateTimeZone.UTC).getYear());
    }

    @Test
    public void testCurrentMonth() {
        assertEquals(date.getCurrentMonth(), new DateTime(DateTimeZone.UTC).getMonthOfYear());
    }

    @Test
    public void testCurrentDay() {
        assertEquals(date.getCurrentDay(), new DateTime(DateTimeZone.UTC).getDayOfMonth());
    }

    @Test
    public void testCurrentHour() {
        assertEquals(date.getCurrentHour(), new DateTime(DateTimeZone.UTC).getHourOfDay());
    }

    @Test
    public void testCurrentMinute() {
        assertEquals(date.getCurrentMinute(), new DateTime(DateTimeZone.UTC).getMinuteOfHour());
    }

    @Test
    public void testCurrentSecond() {
        assertEquals(date.getCurrentSecond(), new DateTime(DateTimeZone.UTC).getSecondOfMinute());
    }
}
