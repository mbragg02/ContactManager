import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Main {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");
	private ContactManager manager = new ContactManagerImpl();
	private Scanner in = new Scanner(System.in);


	public static void main(String[] args) throws ParseException {
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
			System.out.println("> Enter EXIT to exit");

			choice = getUserInput();


			if (choice.matches("[0-9]+")) {
				int choiceNumber = toInteger(choice);
				if (choiceNumber > 4 || choiceNumber < 1) {
					System.out.println("Not a valid option.");
				} else {
					menu(choiceNumber);
				}

			} 
			else {
				if (choice.equals("exit")) {
					running = false;
					break;
				} else {
					System.out.println("Not a valid option.");
				}

			}

		}
		in.close();
		System.out.println("Exit succesfull");

		//		
		//		Calendar calHolder = Calendar.getInstance();
		//		
		//		String testDate = "01/01/2014 12:00";
		//		Date newDate = df.parse(testDate);
		//		calHolder.setTime(newDate);
		//		System.out.println(newDate);
		//		for (Contact x : manager.getContacts()) {
		//			System.out.println(x.getId());
		//		}

		//		Set<Contact> temp = (Set<Contact>) manager.getContacts();
		//		manager.addFutureMeeting(temp, calHolder );

	}

	private void menu(int choice) {
		System.out.println("Main Menu");

		switch(choice) {
		case 1: // add a new contact
			System.out.print("Please enter the name for a new contact: ");
			String name = getUserInput();
			System.out.print("Now enter and notes you wish to store woth this contact: ");
			String notes = getUserInput();
			manager.addNewContact(name, notes);
			System.out.println("Thankyou, Your contact has been successfully added.\n");

			break;
		case 2: // add a new meeting
			addMeeting();
			break;
		case 3: // display a contacts details
			break;
		case 4: // display a meetings details
			break;
		default: // default
			break;
		}
	}

	private void addMeeting() {
		boolean running = false;
		boolean future = false;


		do {
			System.out.print("Would you like to add a future meeting [F], or a meeting that has happened in the past [P]: ");
			String choice = getUserInput();
			if (choice.equals("f") || choice.equals("p")) {
				if (choice.equals("f") ? future = true : false);
				running = false;
			} else {
				System.out.println("Not a valid choice.");
				running = true;
			}

		} while (running);
		
		
		System.out.print("Please list the names of the contacts attending this meeting, seperated by commas: ");
		String contacts = getUserInput();
		String[] names = contacts.trim().split("\\s*,\\s*");
		Set<Contact> meetingContacts = new HashSet<Contact>();
		for (String name : names) {
			for (Contact x : manager.getContacts()) {
				if (x.getName().equals(name)) {
					meetingContacts.add(x);
					System.out.println(name + " successfully added to the meeting");
				} 
			}
		}

		System.out.println("New please enter a date for the meeting (DD/MM/YYYY HH:MM): ");
		String date = getUserInput();
		Calendar meetingDate = getDate(date);
		
		if(future) {
			manager.addFutureMeeting(meetingContacts, meetingDate);
		} else {
			System.out.print("Please enter a note for the meeting: ");
			String note = getUserInput();

			manager.addNewPastMeeting(meetingContacts, meetingDate, note);
		}
		System.out.println("Meeting succesfully scheduled");
	}

	private Calendar getDate(String date) {
		Calendar cal = Calendar.getInstance();
		Date newdate = null;
		try {
			newdate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(newdate);
		return cal;
	}

	private int toInteger(String str) {
		int result = Integer.parseInt(str.trim());
		return result;
	}

	private String getUserInput() {
		String result = "";		
		result = in.nextLine();
		return result.trim().toLowerCase();

	}



}
