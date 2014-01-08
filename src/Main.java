import impl.ContactManagerImpl;
import utilities.Util;
import interfaces.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Actions to perform in Contact Manager
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

	/**
	 * Choice 1: Add a new contact
	 * @return void
	 */
	void addContact() {
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
			System.err.println(ex.getMessage());
		}
		Util.println("\n" + name.toUpperCase() + " has been successfully added to your contacts.\n");
	}

	/**
	 * Choice 2: Add new meeting
	 * Create either a meeting in the past or the future.
	 * @return void
	 */
	void addMeeting() {
		boolean future = pastOrFuture();

		Set<Contact> meetingContacts = new HashSet<Contact>();
		String[] names = null;

		do {
			names = contactsMatcher();
		} while (names == null);

		for (String name : names) {
			for (Contact contact : manager.getContacts(name)) {
				meetingContacts.add(contact);
				Util.println(name + " added to meeting.");
			}
		}

		Calendar meetingDate = null;
		String date = null;
		boolean valid = true;
		do {
			valid = true;
			System.out.print("Please enter a date for the meeting (DD/MM/YYYY HH:MM): ");
			date = getUserInput();
			if(!Util.dateTimeMatcher(date)) {
				Util.println("Date not valid. Please try again.");
				valid = false;
			}

			if(valid) {
				meetingDate = Util.parseDate(date);

				if(future) {	
					try {
						manager.addFutureMeeting(meetingContacts, meetingDate);
					} catch (IllegalArgumentException ex) {
						Util.println("A future meeting's date must be in the future");
						valid = false;
					}
				} 
				if (!future) {
					System.out.print("Please enter a note for the meeting: ");
					String note = getUserInput();
					try {
						manager.addNewPastMeeting(meetingContacts, meetingDate, note);
					} catch (IllegalArgumentException ex) {
						Util.println("Date must be in the past");
						valid = false;
					}
				}
			}

		} while (!valid);
		Util.println("Meeting succesfully scheduled.");
	}

	/**
	 * Choice 3: Display contact details.
	 * Displays either a single contacts details supplied by their name.
	 * Or displays multiple contact details supplied by a number of contact IDs
	 * @return void
	 */
	void displayContactDetails() {
		System.out.print("Please enter a contact name or a series of contact IDs seperated by commas: ");
		String input = getUserInput();

		if (input.isEmpty()) {
			Util.println("No input detected");
		}

		else if (Character.isDigit(input.charAt(0))) {
			// user has entered ids
			List<String> idsQuery = Arrays.asList(input.split("\\s*,\\s*"));
			int[] ids = new int [idsQuery.size()];

			for (int i = 0; i < idsQuery.size(); i ++ ) {
				ids[i] = Util.toInteger(idsQuery.get(i));
			}

			try {
				Util.printContacts(manager.getContacts(ids));
			} catch (IllegalArgumentException ex) {
				Util.println(ex.getMessage());
			}

		} else {
			// user has entered a string
			if (manager.getContacts(input).isEmpty()) {
				Util.println("\"" + input + "\" is not one of your contacts.\n");
				return;
			} 

			try{
				Util.printContacts(manager.getContacts(input));
			} catch (NullPointerException ex) {
				Util.println("Contact name is null");
			}
		}
	}


	/**
	 * Choice 4: Display Meeting details. 
	 * Gives the user an option for either a past meeting or a future meeting.
	 */
	void displayMeetingDetails() {
		boolean future = pastOrFuture();
		if (future) {
			displayFutureMeeting();
		} else {
			displayPastMeeting();
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

		if (Util.dateTimeMatcher(input)) {
			// a date has been entered
			if (manager.getFutureMeetingList(Util.parseDate(input)).isEmpty()) {
				Util.println("No meetings found scheduled for " + input + "\n");
			} else {
				Util.printMeetingList(manager.getFutureMeetingList(Util.parseDate(input)));

			}
		} 
		else if (Character.isDigit(input.charAt(0))) {
			// an id has been entered
			try {
				Util.printMeeting(manager.getFutureMeeting(Util.toInteger(input)));

			} catch(IllegalArgumentException ex) {
				Util.println("A meeting with id: " + input + " is in the past.\n");
			} catch (NullPointerException ex) {
				Util.println("No meeting found\n");
			}

		} 
		else {
			// a contact name has been entered
			if (manager.getContacts(input).isEmpty()) {
				Util.println("\"" + input + "\" is not one of your contacts.\n");
			} 
			List<Meeting> futureMeetings = null;
			for (Contact x : manager.getContacts(input)) {
				try {
					futureMeetings = manager.getFutureMeetingList(x);
				} catch (IllegalArgumentException ex) {
					Util.println("Not a valid contact\n");
				}
			}
			try {
				Util.printMeetingList(futureMeetings);
			} catch (NullPointerException ex) {
				Util.println("Nothing to display");
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
			// an id has been entered
			int id = Util.toInteger(input);
			try {
				Util.printMeeting(manager.getPastMeeting(id));
			} catch (IllegalArgumentException ex) {
				Util.println("A meeting matchting that ID was found in the future.\n");
			} catch (NullPointerException ex) {
				Util.println("No meeting found\n");
			}
		} 
		else {
			// a contact name has been entered

			if (manager.getContacts(input).isEmpty()) {
				Util.println("\"" + input + "\" is not one of your contacts.");
			} 
			List<PastMeeting> pastMeetings = null;
			for (Contact x : manager.getContacts(input)) {
				try {
					pastMeetings = manager.getPastMeetingList(x);
				} catch (IllegalArgumentException ex) {
					Util.println("Not a valid contact");
				}
			}
			try {
				Util.printPastMeetingList(pastMeetings);
			} catch (NullPointerException ex) {
				Util.println("Nothing to print");
			}
		}
	}

	/**
	 * Choice 5. Add a note to a meeting
	 */
	void addMeetingNote() {
		System.out.print("Please enter the ID for the meeting: ");
		int id = Util.toInteger(getUserInput());
		System.out.print("Please enter your meeting note: ");
		String note = getUserInput();
		try {
			manager.addMeetingNotes(id, note);
			Util.println("Note successfuly added to meeting: " + id );
			Util.printMeeting(manager.getPastMeeting(id));
			Util.println("");

		} catch (IllegalArgumentException ex) {
			Util.println(ex.getMessage());
		} catch (IllegalStateException ex) {
			Util.println(ex.getMessage());
		} catch (NullPointerException ex) {
			Util.println(ex.getMessage());
		}

	}

	/**
	 * Choice (6) EXIT
	 */
	void save(){
		manager.flush();
	}




	// Supporting private methods. User interaction. Use of Scanner in
	/**
	 * Method to get an input from the user.
	 * @return String. The users input.
	 */
	private String getUserInput() {
		return in.nextLine().trim().toLowerCase();
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
				Util.println(name + " not found");
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
				Util.println("Not a valid choice. Please try again");
				running = true;
			}

		} while (running);

		return result;
	}

}
