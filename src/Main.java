import impl.ContactManagerImpl;
import interfaces.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contacts Manager Application v1.1
 * @author Michael Bragg
 *
 */
public class Main {

	private ContactManager manager;
	private Scanner in;

	public Main() {
		in = new Scanner(System.in);
		manager = new ContactManagerImpl();
	}

	public static void main(String[] args) {
		Main launcher = new Main();
		launcher.launch();
	}

	public void launch() {

		boolean running = true;
		String choice = "";
		System.out.println("  Welcome to Contact Managaer v1");
		while(running) {

			System.out.println("> Enter 1 to add a new contact");
			System.out.println("> Enter 2 to add a new meeting");
			System.out.println("> Enter 3 to view a contacts details");
			System.out.println("> Enter 4 to view a meetings details");
			System.out.println("> Enter 5 to add a note to a meeting");
			System.out.println("> Enter EXIT to close");
			System.out.print(": ");

			choice = getUserInput();

			if (choice.matches("[0-5]")) {
				int choiceNumber = toInteger(choice);
				menu(choiceNumber);
			} 
			else {
				if (choice.equals("exit")) {
					manager.flush();
					running = false;
					break;
				} else {
					System.out.println("Not a valid option. Please try again");
				}

			}

		}
		in.close();
		System.out.println("Exit succesfull");
	}

	/**
	 * Main application menu
	 * @param choice int 1 - 5
	 */
	private void menu(int choice) {
		switch(choice) {
		case 1: // add a new contact
			addContact();
			break;
		case 2: // add a new meeting
			addMeeting();
			break;
		case 3: // display a contacts details
			displayContactDetails();
			break;
		case 4: // display a meetings details
			displayMeetingDetails();
			break;
		case 5: // add note to meeting
			addMeetingNote();
			break;
		default: 
			System.out.println("Input must be between 1 and 5");
			break;
		}
	}

	/**
	 * Choice 1: Add a new contact
	 * @return void
	 */
	private void addContact() {
		String name = "";
		do {
			System.out.print("Please enter the name for a new contact: ");
			name = getUserInput();
		} while(name.length() < 1);

		System.out.print("Now enter any notes for this contact: ");
		String notes = getUserInput();
		try {
			manager.addNewContact(name, notes);
		} catch (NullPointerException ex) {
			System.err.println(ex);
		}
		System.out.println(name.toUpperCase() + " has been successfully added to your contacts.\n");
	}

	/**
	 * Choice 2: Add new meeting
	 * Create either a meeting in the past or the future.
	 * @return void
	 */
	private void addMeeting() {
		boolean future = pastOrFuture();

		Set<Contact> meetingContacts = new HashSet<Contact>();
		String[] names = null;

		do {
			names = contactsMatcher();

		} while (names == null);

		for (String x : names) {
			for (Contact y : manager.getContacts(x)) {
				meetingContacts.add(y);
				System.out.println(x + " added to meeting.");
			}
		}

		Calendar meetingDate = null;
		String date = null;
		boolean valid = true;
		do {
			valid = true;
			System.out.print("Please enter a date for the meeting (DD/MM/YYYY HH:MM): ");
			date = getUserInput();
			if(!dateTimeMatcher(date)) {
				System.out.println("Date not valid. Please try again.");
				valid = false;
			}

			if(valid) {
				meetingDate = parseDate(date);

				if(future) {	
					try {
						manager.addFutureMeeting(meetingContacts, meetingDate);
					} catch (IllegalArgumentException ex) {
						System.out.println("A future meeting's date must be in the future");
						valid = false;
					}
				} 
				if (!future) {
					System.out.print("Please enter a note for the meeting: ");
					String note = getUserInput();
					try {
						manager.addNewPastMeeting(meetingContacts, meetingDate, note);
					} catch (IllegalArgumentException ex) {
						System.out.println("Date must be in the past");
						valid = false;
					}
				}
			}

		} while (!valid);


		System.out.println("Meeting succesfully scheduled.");
	}

	/**
	 * Choice 3: Display contact details.
	 * Displays either a single contacts details supplied by their name.
	 * Or displays multiple contact details supplied by a number of contact IDs
	 * @return void
	 */
	private void displayContactDetails() {
		System.out.print("Please enter a contact name or a series of contact IDs seperated by commas: ");
		String input = getUserInput();

		if (input.isEmpty()) {
			System.out.println("No input detected");
		}

		else if (Character.isDigit(input.charAt(0))) {
			// user has entered ids
			List<String> idsQuery = Arrays.asList(input.split("\\s*,\\s*"));
			int [] ids = new int [idsQuery.size()];

			for (int i = 0; i < idsQuery.size(); i ++ ) {
				ids[i] = toInteger(idsQuery.get(i));
			}

			try {
				printContacts(manager.getContacts(ids));
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}

		} else {
			// user has entered a string
			if (manager.getContacts(input).isEmpty()) {
				System.out.println("\"" + input + "\" is not one of your contacts.\n");
				return;
			} 

			try{
				printContacts(manager.getContacts(input));
			} catch (NullPointerException ex) {
				System.out.println("Name is null");
			}
		}
	}


	/**
	 * Choice 4: Display Meeting details. 
	 * Gives the user an option for either a past meeting or a future meeting.
	 */
	private void displayMeetingDetails() {
		boolean future = pastOrFuture();
		if (future) {
			displayFutureMeeting();
		} else {
			displayPastMeeting();
		}
	}

	/**
	 * Choice 5. Add a note to a meeting
	 */
	private void addMeetingNote() {
		System.out.print("Please enter the ID for the meeting: ");
		int id = toInteger(getUserInput());
		System.out.print("Please enter your meeting note: ");
		String note = getUserInput();
		try {
			manager.addMeetingNotes(id, note);
			System.out.println("Note successfuly added to meeting: " + id );
			printMeeting(manager.getPastMeeting(id));
			System.out.println();
			
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		} catch (IllegalStateException ex) {
			System.out.println(ex.getMessage());
		} catch (NullPointerException ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * Displays Future Meeting information. Gives the user an option enter either:
	 * A meeting ID
	 * A contact name
	 * or a date for a meeting.
	 */
	private void displayFutureMeeting() {
		String input;
		do {
			System.out.print("Please enther either a meeting ID, a contact name, or a date one which a meeting is to take place (DD/MM/YYYY hh:mm): ");
			input = getUserInput();
		} while (input.isEmpty());

		if (dateTimeMatcher(input)) {
			// a date
			if (manager.getFutureMeetingList(parseDate(input)).isEmpty()) {
				System.out.println("No meetings found scheduled for " + input);
			} else {
				printMeetingList(manager.getFutureMeetingList(parseDate(input)));

			}
		} 
		else if (Character.isDigit(input.charAt(0))) {
			// an id
			try {
				printMeeting(manager.getFutureMeeting(toInteger(input)));

			} catch(IllegalArgumentException ex) {
				System.out.println("A meeting with id: " + input + " is in the past.");
			} catch (NullPointerException ex) {
				System.out.println("Nothing to display");
			}

		} 
		else {
			// a contact name
			if (manager.getContacts(input).isEmpty()) {
				System.out.println("\"" + input + "\" is not one of your contacts.");
			} 
			List<Meeting> futureMeetings = null;
			for (Contact x : manager.getContacts(input)) {
				try {
					futureMeetings = manager.getFutureMeetingList(x);
				} catch (IllegalArgumentException ex) {
					System.out.println("Not a valid contact");
				}
			}
			try {
				printMeetingList(futureMeetings);
			} catch (NullPointerException ex) {
				System.out.println("Nothing to display");
			}
		}
	}


	/**
	 * Displays Past Meeting information. Gives the user an option enter either:
	 * A meeting ID
	 * A contact name
	 */
	private void displayPastMeeting() {
		String input;
		do {
			System.out.print("Please enter either a meeting ID or a contact name to view all of the meetings they have attended: ");
			input = getUserInput();
		} while (input.isEmpty());

		if (Character.isDigit(input.charAt(0))) {
			// an id
			int id = toInteger(input);
			try {
				printMeeting(manager.getPastMeeting(id));
			} catch (NullPointerException ex) {
				System.out.println("Nothing to print");
			} catch (IllegalArgumentException ex) {
				System.out.println("A meeting matchting that ID was found in the future.");
			}
		} 
		else {
			// a contact name

			if (manager.getContacts(input).isEmpty()) {
				System.out.println("\"" + input + "\" is not one of your contacts.");
			} 
			List<PastMeeting> pastMeetings = null;
			for (Contact x : manager.getContacts(input)) {
				try {
					pastMeetings = manager.getPastMeetingList(x);
				} catch (IllegalArgumentException ex) {
					System.out.println("Not a valid contact");
				}

			}
			try {
				printPastMeetingList(pastMeetings);
			} catch (NullPointerException ex) {
				System.out.println("Nothing to print");
			}
		}
	}


	// Supporting private methods

	/**
	 * Method to get an input from the user.
	 * @return String. The users input.
	 */
	private String getUserInput() {
		String result = in.nextLine();
		return result.trim().toLowerCase();
	}


	/**
	 * Converts a string to a int.
	 * @param str String to convert
	 * @return int representation of the supplied string.
	 */
	private int toInteger(String str) {
		int result = Integer.parseInt(str.trim());
		return result;
	}


	/**
	 * Asks the user for a series of contact names (separated by commas).
	 * If all the names are valid contacts, then it returns an array of the names.
	 * Otherwise it returns null.
	 * @return String[] contact names
	 */
	private String[] contactsMatcher() {
		String[] names = null;
		String contacts = null;

		System.out.print("Please list the names of the contacts attending this meeting, seperated by commas: ");
		contacts = getUserInput();
		names = contacts.trim().split("\\s*,\\s*");
		boolean valid = true;
		for (String name : names) {
			if (manager.getContacts(name).size() == 0) {
				System.out.println(name + " not found");
				valid = false;
			} 
		}
		if (!valid) {
			return null;
		}

		return names;
	}


	/**
	 * Gives a user the option to select a future meeting or past meeting.
	 * Checks the user input for valid options.
	 * @return Boolean. YES for future. NO for past
	 */
	private boolean pastOrFuture() {
		boolean running = false;
		boolean result = false;

		do {
			System.out.print("For a future meeting type [F], or for a meeting that has happened in the past, [P]: ");
			String choice = getUserInput();
			if (choice.equals("f") || choice.equals("p")) {
				if (choice.equals("f") ? result = true : false);
				running = false;
			} else {
				System.out.println("Not a valid choice. Please try again");
				running = true;
			}

		} while (running);

		return result;
	}

	/**
	 * Matches a supplied string with a regular expression for the date format:
	 * DD/MM/YYYY HH:MM
	 * @param str String to match.
	 * @return Boolean. A match or not
	 */
	private boolean dateTimeMatcher(String str) {
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
	 * @param str String to match.
	 * @return Boolean. A match or not
	 */
	private boolean dateMatcher(String str) {
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
	 * Returns a Calendar for the given date string
	 * @param date String.
	 * @return Calendar
	 */
	private Calendar parseDate(String date) {
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

	/**
	 * Returns a Calendar for the given date string
	 * @param date String.
	 * @return Calendar
	 */
	private Calendar getDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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




	/**
	 * Method to print out a set of contacts
	 * @param contacts Set<Contact> contacts
	 */
	private void printContacts(Set<Contact> contacts) {
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
	private void printMeeting(Meeting meeting) {
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
	private void printPastMeetingList(List<PastMeeting> list) {
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
	private void printMeetingList(List<Meeting> list) {
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
