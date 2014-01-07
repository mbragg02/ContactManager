import java.util.Scanner;
import utilities.Util;

/**
 * Main menu loop for Contact Manager
 * @author Michael Bragg
 *
 */
public class MainMenu {

	private Main main;

	public MainMenu() {
		main = new Main();
	}

	public void launch() {
		
		Scanner in = new Scanner(System.in);
		boolean running = true;
		
		String choice = "";
		System.out.println("  Welcome to Contact Managaer v1");
		while(running) {
			System.out.println("  Main Menu");
			System.out.println("> Enter 1 to add a new contact");
			System.out.println("> Enter 2 to add a new meeting");
			System.out.println("> Enter 3 to view a contacts details");
			System.out.println("> Enter 4 to view a meetings details");
			System.out.println("> Enter 5 to add a note to a meeting");
			System.out.println("> Enter EXIT to save data and exit");
			System.out.print(": ");

			choice = in.nextLine().trim();
			
			if (choice.toLowerCase().equals("exit")) {
				main.save();
				running = false;
				break;
			} 
			else {
				try {
					int choiceNumber = Util.toInteger(choice);
					menu(choiceNumber);
				} catch (NumberFormatException ex) {
					System.out.println("Not a valid option. Please try again");
				} catch (IllegalArgumentException ex) {
					System.out.println("Must be between 1 and 5. Please try again");
				}
			}
				
		} // end of running loop
		in.close();
		System.out.println("Exit succesfull");
	}


	/**
	 * Main application menu
	 * @param choice int 1 to 5
	 * @throws IllegalArgumentException. Input must be between 1 and 5
	 */
	private void menu(int choice) {

		switch(choice) {
		case 1: // add a new contact
			main.addContact();
			break;
		case 2: // add a new meeting
			main.addMeeting();
			break;
		case 3: // display a contacts details
			main.displayContactDetails();
			break;
		case 4: // display a meetings details
			main.displayMeetingDetails();
			break;
		case 5: // add note to meeting
			main.addMeetingNote();
			break;
		default: 
			throw new IllegalArgumentException("Input must be between 1 and 5");
		}
	}

}
