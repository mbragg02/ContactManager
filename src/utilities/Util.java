package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import interfaces.*;

/**
 * Methods to support the Main actions for Contact Manager
 * @author Michael Bragg
 */
public abstract class Util {

	/**
	 * Converts a string to a int.
	 * @param str String to convert
	 * @return int representation of the supplied string.
	 * @throws NumberFormatException if the string does not contain a parsable integer.
	 */
	public static int toInteger(String str) throws NumberFormatException {
		return Integer.parseInt(str.trim());
	}
	
	/**
	 * Checks if a given string contains only digits.
	 * @param str
	 * @return boolean
	 */
	public static boolean idValidater(String str) {
		if (str.matches("[0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	// Calendar/Date methods
	/**
	 * Method to distinguish between a id string (a string containing just digits) and a date string (a string containing digits and forward slashes)
	 * @param str String to check.
	 * @return Boolean. True if it is a date string. Otherwise false.
	 */
	public static boolean datePatternValidater(String str) {
		if (Character.isDigit(str.charAt(0)) && str.contains("/")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Matches a supplied string with a regular expression for the date format:
	 * DD/MM/YYYY HH:MM
	 * @param str String to match.
	 * @return Boolean. A match or not
	 * @throws PatternSyntaxException If the expression's syntax is invalid
	 */
	public static boolean dateValidater(String str) throws PatternSyntaxException {
		boolean result = false;
		// DD/MM/YYYY HH/MM
		Pattern p = Pattern.compile("([0-2][1-9]|[1-3]0|31)/(0[1-9]|10|11|12)/([0-9]{4})\\s([0-1][0-9]|2[0-3]):([0-5][0-9])");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Matches a supplied string with a regular expression for the date format:
	 * DD/MM/YYYY
	 * @param str
	 * @return Boolean. A match or not
	 * @throws PatternSyntaxException If the expression's syntax is invalid
	 */
	public static boolean calendarValidater(String str) throws PatternSyntaxException {
		boolean result = false;
		// DD/MM/YYYY
		Pattern p = Pattern.compile("([0-2][1-9]|[1-3]0|31)/(0[1-9]|10|11|12)/([0-9]{4})");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * Returns a Calendar for the given date string, with the format DD/MM/YYYY HH/MM
	 * @param date String.
	 * @return Calendar
	 */
	public static Calendar parseDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");

		Calendar calendar = Calendar.getInstance();
		try {
			Date newdate = dateFormat.parse(date);
			calendar.setTime(newdate);
		} catch (ParseException e) {
			println("Invalid date format");
			e.printStackTrace();
		}
		return calendar;
	}
	
	/**
	 * Returns a Calendar for the given date string, with the format DD/MM/YYYY
	 * @param date String.
	 * @return Calendar
	 */
	public static Calendar parseCalendar(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Calendar calendar = Calendar.getInstance();
		try {
			Date newdate = dateFormat.parse(date);
			calendar.setTime(newdate);

		} catch (ParseException e) {
			println("Invalid date format");
			e.printStackTrace();
		}
		return calendar;
	}
	
	/**
	 * Throws an IllegalArgumentException if a supplied date is in the past.
	 * @param date
	 * @throws IllegalArgumentException if a supplied date is in the past.
	 */
	public static void dateInFutureCheck(Calendar date) throws IllegalArgumentException {
		Calendar calendar = Calendar.getInstance();

		if (date.getTime().before(calendar.getTime())) {
			throw new IllegalArgumentException("Meeting is in the Past");
		}
	}
	
	/**
	 * Returns a cleared calendar object with just the day/month/year data from the supplied calendar.
	 * @param calendar
	 * @return Calendar
	 */
	public static Calendar setCalendarTime(Calendar calendar) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DATE);
		cal.set(year, month, date);	
		return cal;
	}


	// Methods to print out information
	/**
	 * Prints a string to the output stream.
	 * i.e System.out.println(String);
	 * @param str
	 */
	public static void println(String str){
		System.out.println(str);
	}

	/**
	 * Method to print out a set of contacts
	 * @param contacts Set<Contact> contacts
	 */
	public static void printContacts(Set<Contact> contacts) {
		Util.println("");
		for (Contact x : contacts) {
			println("ID: " + x.getId());
			println("Name: " + x.getName());
			println("Notes: " + x.getNotes());
			println("");
		}
	}

	/**
	 * Prints out a meeting's details
	 * @param meeting Meeting
	 */
	public static void printMeeting(Meeting meeting) {
		println("");
		println("Meeting id: " + meeting.getId());
		println("on: " + meeting.getDate().getTime());
		if (meeting instanceof PastMeeting) {
			println("notes: "  + ((PastMeeting) meeting).getNotes());
		}
		println("");
	}

	/**
	 * Prints out a list of Past Meetings
	 * @param list List<PastMeeting>
	 */
	public static void printPastMeetingList(List<PastMeeting> list) {
		if (list.isEmpty()) {
			println("No past meetings scheduled");
		}
		System.out.println();
		for (PastMeeting x : list) {
			println("Meeting id: " + x.getId());
			println("on: " + x.getDate().getTime());
			println("notes: " + x.getNotes());
			println("");
		}

	}

	/**
	 * Prints out a list of meetings
	 * @param list List<Meeting>
	 */
	public static void printMeetingList(List<Meeting> list) {
		if (list.isEmpty()) {
			println("No future meetings scheduled");
		}
		System.out.println();
		for (Meeting x : list) {
			println("Meeting id: " + x.getId());
			println("on: " + x.getDate().getTime());
			println("");
		}

	}

}
