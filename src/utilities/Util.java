package utilities;

import interfaces.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods to support the Main actions for Contact Manager
 * @author Michael Bragg
 *
 */
public abstract class Util {

	/**
	 * Converts a string to a int.
	 * @param str String to convert
	 * @return int representation of the supplied string.
	 * @throws NumberFormatException
	 */
	public static int toInteger(String str) {
		return Integer.parseInt(str.trim());
	}

	// Calendar/Date methods
	/**
	 * Matches a supplied string with a regular expression for the date format:
	 * DD/MM/YYYY HH:MM
	 * @param str String to match.
	 * @return Boolean. A match or not
	 */
	public static boolean dateTimeMatcher(String str) {
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
	 * Returns a Calendar for the given date string
	 * @param date String.
	 * @return Calendar
	 */
	public static Calendar parseDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");

		Calendar calendar = Calendar.getInstance();
		Date newdate = null;
		try {
			newdate = dateFormat.parse(date);
		} catch (ParseException e) {
			System.out.println("Invalid date format");
			e.printStackTrace();
		}
		calendar.setTime(newdate);
		return calendar;
	}

//	/**
//	 * Matches a supplied string with a regular expression for the date format:
//	 * DD/MM/YYYY
//	 * @param str String to match.
//	 * @return Boolean. A match or not
//	 */
//	private boolean dateMatcher(String str) {
//		boolean result = false;
//		// DD/MM/YYYY
//		Pattern p = Pattern.compile("([0-2][1-9]|[1-3]0|31)/(0[1-9]|10|11|12)/([0-9]{4})");
//		Matcher m = p.matcher(str);
//		if (m.matches()) {
//			result = true;
//		}
//		return result;
//	}
//
//
//
//	/**
//	 * Returns a Calendar for the given date string
//	 * @param date String.
//	 * @return Calendar
//	 */
//	private Calendar getDate(String date) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//		Calendar calendar = Calendar.getInstance();
//		Date newdate = null;
//		try {
//			newdate = dateFormat.parse(date);
//		} catch (ParseException e) {
//			System.out.println("Invalid date format");
//			e.printStackTrace();
//		}
//		calendar.setTime(newdate);
//		return calendar;
//	}



	// Methods to print out information
	/**
	 * Method to print out a set of contacts
	 * @param contacts Set<Contact> contacts
	 */
	public static void printContacts(Set<Contact> contacts) {
		System.out.println();
		for (Contact x : contacts) {
			System.out.println("ID: " + x.getId());
			System.out.println("Name: " + x.getName());
			System.out.println("Notes: " + x.getNotes());
			System.out.println();
		}
	}

	/**
	 * Prints out a meeting's details
	 * @param meeting Meeting
	 */
	public static void printMeeting(Meeting meeting) {
		System.out.println();
		System.out.println("Meeting id: " + meeting.getId());
		System.out.println("on: " + meeting.getDate().getTime());
		if (meeting instanceof PastMeeting) {
			System.out.println("notes: "  + ((PastMeeting) meeting).getNotes());
		}
	}

	/**
	 * Prints out a list of Past Meetings
	 * @param list List<PastMeeting>
	 */
	public static void printPastMeetingList(List<PastMeeting> list) {
		if (list.isEmpty()) {
			System.out.println("No past meetings scheduled");
		}
		System.out.println();
		for (PastMeeting x : list) {
			System.out.println("Meeting id: " + x.getId());
			System.out.println("on: " + x.getDate().getTime());
			System.out.println("notes: " + x.getNotes());
			System.out.println();
		}

	}

	/**
	 * Prints out a list of meetings
	 * @param list List<Meeting>
	 */
	public static void printMeetingList(List<Meeting> list) {
		if (list.isEmpty()) {
			System.out.println("No future meetings scheduled");
		}
		System.out.println();
		for (Meeting x : list) {
			System.out.println("Meeting id: " + x.getId());
			System.out.println("on: " + x.getDate().getTime());
			System.out.println();
		}

	}

}
