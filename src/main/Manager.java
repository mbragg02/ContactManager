package main;
import utilities.Util;
import interfaces.*;
import java.util.*;

/**
 * Actions to perform in Contact Manager
 * @author Michael Bragg
 *
 */
public class Manager {

	private ContactManager manager;
	private Scanner userInput;

	public Manager(ContactManager manager) {
		userInput = new Scanner(System.in);
		this.manager = manager;
	}

	/**
	 * Choice 1: Add a new contact
	 * @return void
	 */
	protected void addContact() {

		String contactName = assignContactName();
		String conactNotes = assignContactNotes();

		try {
			manager.addNewContact(contactName, conactNotes);
		} catch (NullPointerException ex) {
			System.err.println(ex.getMessage());
		}
		Util.println("\n" + contactName.toUpperCase() + " has been successfully added to your contacts.\n");
	}

	private String assignContactName() {
		String contactName;
		do {
			System.out.print("Please enter the name for a new contact: ");
			contactName = getUserInput();
		} while(contactName.length() < 1);
		return contactName;
	}

	private String assignContactNotes() {
		System.out.print("Now enter any notes for this contact: ");
		String contactNotes = getUserInput();
		return contactNotes;
	}



	/**
	 * Choice 2: Add new meeting
	 * Create either a meeting in the past or the future.
	 * @return void
	 */
	protected void addMeeting() {

		boolean future = assignPastOrFutureMeeting();
		String[] contactNames = assignContactsNames();
		Set<Contact> meetingContacts = assignMeetingContacts(contactNames);
		Calendar meetingDate = assignMeetingDate();

		if(future) {
			addFutureMeeting(meetingContacts, meetingDate);
		} else {
			String meetingNote = assignMeetingNote();
			addPastMeeting(meetingContacts, meetingDate, meetingNote);
		}

		Util.println("Meeting succesfully scheduled.");
	}


	/**
	 * Asks the user for a series of contact names (separated by commas).
	 * If all the names are valid contacts, then it returns an array of the names.
	 * Otherwise it returns null.
	 * @return String[] contact names
	 */
	private String[] assignContactsNames() {
		String [] contactNames = null;
		String userInput = null;
		boolean valid;

		do {
			System.out.print("Please list the names of the contacts attending this meeting, seperated by commas: ");
			userInput = getUserInput();
			if (userInput.isEmpty()) {
				valid = false;
			}
			else {
				contactNames = userInput.trim().split("\\s*,\\s*");
				valid = validateContactsNames(contactNames);
			} 
		} while (!valid);

		return contactNames;
	}

	private boolean validateContactsNames(String[] contactNames) {
		boolean valid = true;
		for (String name : contactNames) {
			if (manager.getContacts(name).size() == 0) {
				Util.println(name + " not found");
				valid = false;
			} 
		}
		return valid;
	}


	private Set<Contact> assignMeetingContacts(String[] contactNames) {
		Set<Contact> contacts = new HashSet<Contact>();
		for (String name : contactNames) {
			for (Contact contact : manager.getContacts(name)) {
				contacts.add(contact);
				Util.println(contact.getName() + " added to meeting.");
			}
		}
		return contacts;
	}

	private Calendar assignMeetingDate() {
		boolean valid;
		String userDate;
		Calendar meetingDate = null;

		do {
			System.out.print("Please enter a date for the meeting (DD/MM/YYYY HH:MM): ");
			userDate = getUserInput();
			if(Util.dateValidater(userDate)) {
				valid = true;
				meetingDate = Util.parseDate(userDate);
			} else {
				Util.println("Date not valid. Please try again.");
				valid = false;
			}
		} while (!valid); 

		return meetingDate;
	}

	private void addFutureMeeting(Set<Contact> contacts, Calendar meetingDate) {
		try {
			manager.addFutureMeeting(contacts, meetingDate);
		} catch (IllegalArgumentException ex) {
			Util.println("A future meeting's date must be in the future");
		}
	}

	private String assignMeetingNote() {
		System.out.print("Please enter a note for the meeting: ");
		String meetingNote = getUserInput();
		return meetingNote;

	}

	private void addPastMeeting(Set<Contact> meetingContacts, Calendar meetingDate, String meetingNote) {

		try {
			manager.addNewPastMeeting(meetingContacts, meetingDate, meetingNote);
		} catch (IllegalArgumentException ex) {
			Util.println("Date must be in the past");
		}
	}



	/**
	 * Choice 3: Display contact details.
	 * Displays either a single contacts details supplied by their name.
	 * Or displays multiple contact details supplied by a number of contact IDs
	 * @return void
	 */
	protected void displayContactDetails() {
		System.out.print("Please enter a contact name or a series of contact IDs seperated by commas: ");
		String userInput = getUserInput();

		if (userInput.isEmpty()) {
			Util.println("No input detected");
		}
		else if (Character.isDigit(userInput.charAt(0))) {
			displayContactsFromId(userInput);
		} 
		else {
			displayContactsFromName(userInput);
		}
	}


	private void displayContactsFromId(String userInput) {
		// user has entered ids
		int[] contactIds = convertToIntArray(userInput);

		try {
			Util.printContacts(manager.getContacts(contactIds));
		} catch (IllegalArgumentException ex) {
			Util.println(ex.getMessage());
		}
	}

	private int[] convertToIntArray(String userInput) {
		List<String> userInputIds = Arrays.asList(userInput.split("\\s*,\\s*"));

		int[] contactIds = new int [userInputIds.size()];

		for (int i = 0; i < userInputIds.size(); i ++ ) {
			contactIds[i] = Util.toInteger(userInputIds.get(i));
		}
		return contactIds;
	}

	private void displayContactsFromName(String userInput) {
		// user has entered a string
		if (manager.getContacts(userInput).isEmpty()) {
			Util.println("\"" + userInput + "\" is not one of your contacts.\n");
		} 
		else {
			try{
				Util.printContacts(manager.getContacts(userInput));
			} catch (NullPointerException ex) {
				Util.println("Contact name is null");
			}
		}
	}



	/**
	 * Choice 4: Display Meeting details. 
	 * Gives the user an option for either a past meeting or a future meeting.
	 */
	protected void displayMeetingDetails() {
		boolean future = assignPastOrFutureMeeting();
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
		String userInput;
		do {
			System.out.print("Please enther either a meeting ID, a contact name, or a date one which a meeting is to take place (DD/MM/YYYY ): ");
			userInput = getUserInput();
		} while (userInput.isEmpty());
		if (Util.datePatternValidater(userInput))  {
			displayFutureMeetingFromDate(userInput);
		}
		else if (Util.idValidater(userInput)) {
			displayFutureMeetingFromId(userInput);
		} 
		 else {
			displayFutureMeetingFromContactName(userInput);
		}

	}

	private void displayFutureMeetingFromDate(String userInput) {
		// a date has been entered
		Calendar meetingDate;

		if(Util.calendarValidater(userInput)) {
			meetingDate = Util.parseCalendar(userInput);
			try {
				Util.dateInFutureCheck(meetingDate);
			} catch (IllegalArgumentException ex) {
				Util.println("\nMeeting date is in the past\n");
				return;
			}
			if (manager.getFutureMeetingList(meetingDate).isEmpty()) {
				Util.println("No meetings found scheduled for " + userInput + "\n");
			} else {
				Util.printMeetingList(manager.getFutureMeetingList(meetingDate));
			}

		} else {
			Util.println("Date is not valid.");
		}
	}

	private void displayFutureMeetingFromId(String userInput) {
		// an id has been entered
		try {
			Util.printMeeting(manager.getFutureMeeting(Util.toInteger(userInput)));
		} catch(IllegalArgumentException ex) {
			Util.println("\nA meeting with id: " + userInput + " is in the past.\n");
		} catch (NullPointerException ex) {
			Util.println("No meeting found\n");
		}
	}

	private void displayFutureMeetingFromContactName(String userInput) {
		// a contact name has been entered
		if (manager.getContacts(userInput).isEmpty()) {
			Util.println("\n\"" + userInput + "\" is not one of your contacts.\n");
		} 
		List<Meeting> futureMeetings = null;
		for (Contact x : manager.getContacts(userInput)) {
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


	/**
	 * Displays Past Meeting information. Gives the user an option enter either:
	 * A meeting ID
	 * A contact name
	 */
	private void displayPastMeeting() {
		String userInput;
		do {
			System.out.print("Please enter either a meeting ID or a contact name to view all of the meetings they have attended: ");
			userInput = getUserInput();
		} while (userInput.isEmpty());

		if (Util.idValidater(userInput)) {
			displayPastMeetingFromId(userInput);
		} 
		else {
			displayPastMeetingFromContactName(userInput);
		}
	}

	private void displayPastMeetingFromId(String userInput) {
		// an id has been entered
		int id = Util.toInteger(userInput);
		try {
			Util.printMeeting(manager.getPastMeeting(id));
		} catch (IllegalArgumentException ex) {
			Util.println("A meeting matchting that ID was found in the future.\n");
		} catch (NullPointerException ex) {
			Util.println("No meeting found\n");
		}
	}

	private void displayPastMeetingFromContactName(String userInput) {
		// a contact name has been entered

		if (manager.getContacts(userInput).isEmpty()) {
			Util.println("\"" + userInput + "\" is not one of your contacts.");
		} 
		List<PastMeeting> pastMeetings = null;
		for (Contact x : manager.getContacts(userInput)) {
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

	/**
	 * Choice 5. Add a note to a meeting
	 */
	protected void addMeetingNote() {
		int meetingId = assignMeetingId();
		String meetingNote = assignMeetingNote();

		try {
			manager.addMeetingNotes(meetingId, meetingNote);
			Util.println("Note successfuly added to meeting: " + meetingId );
			Util.printMeeting(manager.getPastMeeting(meetingId));
			Util.println("");

		} catch (IllegalArgumentException ex) {
			Util.println(ex.getMessage());
		} catch (IllegalStateException ex) {
			Util.println(ex.getMessage());
		} catch (NullPointerException ex) {
			Util.println(ex.getMessage());
		}

	}

	private int assignMeetingId() {
		System.out.print("Please enter the ID for the meeting: ");
		int id = Util.toInteger(getUserInput());
		return id;
	}


	/**
	 * Choice (6) EXIT
	 */
	protected void save(){
		manager.flush();
	}


	// Supporting private methods. User interaction. Use of Scanner in
	/**
	 * Method to get an input from the user.
	 * @return String. The users input.
	 */
	private String getUserInput() {
		return userInput.nextLine().trim().toLowerCase();
	}


	/**
	 * Gives a user the option to select a future meeting or past meeting.
	 * Checks the user input for valid options.
	 * @return Boolean. YES for future. NO for past
	 */
	private boolean assignPastOrFutureMeeting() {
		boolean running = false;
		boolean result = false;

		do {
			System.out.print("For a future meeting type [F], or for a meeting that has happened in the past, [P]: ");
			String choice = getUserInput();
			if (choice.equals("f") || choice.equals("p")) {
				result = (choice.equals("f") ? true : false);
				running = false;
			} else {
				Util.println("Not a valid choice. Please try again");
				running = true;
			}

		} while (running);

		return result;
	}

}
