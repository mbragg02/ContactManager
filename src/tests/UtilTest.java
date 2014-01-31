package tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.regex.PatternSyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.Util;

public class UtilTest {
	
	private static Calendar futureDate;
	private static Calendar pastDate;

	@BeforeClass
	public static void setUp() {
		// Create a future and past date
		futureDate = Calendar.getInstance();
		futureDate.set(2015, 1, 1);
		pastDate = Calendar.getInstance();
		pastDate.set(2000, 1, 1);
	}

	@Test
	public void testToInteger() {
		int actual = Util.toInteger("1");
		assertEquals(1, actual);
	}
	@Test(expected = NumberFormatException.class)
	public void testToIntegerException() {
		Util.toInteger("a");
	}

	
	@Test
	public void testIdValidater() {	
		assertTrue(Util.idValidater("1"));
	}
	@Test
	public void testIdValidaterFalse() {	
		assertFalse(Util.idValidater("a"));
	}

	
	@Test
	public void testDatePatternValidater() {
		assertTrue(Util.datePatternValidater("12/12/2012 12:00"));
	}
	@Test
	public void testDatePatternValidaterFalse() {
		assertFalse(Util.datePatternValidater("1"));
	}

	
	@Test
	public void testDateValidater() {
		assertTrue(Util.dateValidater("12/12/2012 12:00"));
	}
	@Test
	public void testDateValidaterFalseMonth() {
		assertFalse(Util.dateValidater("12/13/2012 12:00"));
	}
	@Test
	public void testDateValidaterFalseDay() {
		assertFalse(Util.dateValidater("40/12/2012 12:00"));
	}
	@Test
	public void testDateValidaterFalseYear() {
		assertFalse(Util.dateValidater("12/12/201 12:00"));
	}
	@Test
	public void testDateValidaterFalseTime() {
		assertFalse(Util.dateValidater("12/12/2012 25:00"));
	}

	
	@Test
	public void testCalendarValidater() {
		assertTrue(Util.calendarValidater("12/12/2012"));
	}
	@Test
	public void testCalendarValidaterFalseMonth() {
		assertFalse(Util.calendarValidater("12/13/2012"));
	}
	@Test
	public void testCalendarValidaterFalseDay() {
		assertFalse(Util.calendarValidater("35/12/2012"));
	}
	@Test
	public void testCalendarValidaterFalseYear() {
		assertFalse(Util.calendarValidater("12/12/201"));
	}

	
	@Test
	public void testParseDate() {
		Calendar actual = Util.parseDate("12/12/2012 12:00");
		Calendar expected = Calendar.getInstance();
		expected.clear();
		// Month is zero based, so use month 11 for December
		expected.set(2012, 11, 12, 12, 00);
		assertEquals(expected.getTime(), actual.getTime());
	}

	
	@Test
	public void testParseCalendar() {
		Calendar actual = Util.parseCalendar("12/12/2012");
		Calendar expected = Calendar.getInstance();
		expected.clear();
		// Month is zero based, so use month 11 for December
		expected.set(2012, 11, 12);
		assertEquals(expected.getTime(), actual.getTime());
	}

	
	@Test
	public void testDateInFutureCheck() {
		Util.dateInFutureCheck(futureDate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDateInFutureCheckPast() {
		Util.dateInFutureCheck(pastDate);
	}

	
	@Test
	public void testSetCalendarTime() {
		Calendar actual = Util.setCalendarTime(futureDate);
		Calendar expected = Calendar.getInstance();
		expected.clear();	
		int year = futureDate.get(Calendar.YEAR);
		int month = futureDate.get(Calendar.MONTH);
		int date = futureDate.get(Calendar.DATE);
		expected.set(year, month, date);	
		assertEquals(expected.getTime(), actual.getTime());
	}
}