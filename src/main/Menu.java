package main;
import java.util.Scanner;

import utilities.Util;

/**
 * Main menu loop for Contact Manager
 * @author Michael Bragg
 */
public class Menu {

	private Manager manager;
	private boolean running;
	private Scanner userInput;

	public Menu(Manager manager) {
		this.manager = manager;
		running = true;
		userInput = new Scanner(System.in);
	}
	
	public void launchMenu() {
		Util.println("  Welcome to Contact Managaer v1");
		menuLoop();
	}
	
	private void menuLoop() {	
		while(running) {
			displayActions();
			selectAction();	
		} 
	}

	private void displayActions(){
		Util.println("  Main Menu");
		Util.println("> Enter 1 to add a new contact");
		Util.println("> Enter 2 to add a new meeting");
		Util.println("> Enter 3 to view a contacts details");
		Util.println("> Enter 4 to view a meetings details");
		Util.println("> Enter 5 to add a note to a meeting");
		Util.println("> Enter EXIT to save data and exit");
		Util.println(": ");
	}

	private void selectAction() {
		String userChoice;
		userChoice = userInput.nextLine().trim();
		if (userChoice.toLowerCase().equals("exit")) {
			exit();
		} else {
			mainMenuLaunch(userChoice);
		}
	}

	private void mainMenuLaunch(String userChoice) {
		try {
			int action = Util.toInteger(userChoice);
			mainMenu(action);
		} catch (NumberFormatException ex) {
			Util.println("Not a valid option. Please try again");
		} catch (IllegalArgumentException ex) {
			Util.println("Must be between 1 and 5. Please try again");
		}
	}


	/**
	 * Main application menu
	 * @param action int 1 to 5
	 * @throws IllegalArgumentException. Input must be between 1 and 5
	 */
	private void mainMenu(int action) {
		switch(action) {
		case 1: 
			manager.addContact();
			break;
		case 2: 
			manager.addMeeting();
			break;
		case 3: 
			manager.displayContactDetails();
			break;
		case 4: 
			manager.displayMeetingDetails();
			break;
		case 5: 
			manager.addMeetingNote();
			break;
		default: 
			throw new IllegalArgumentException("Input must be between 1 and 5");
		}
	}

	private void exit() {
		manager.save();
		running = false;
		userInput.close();
		Util.println("Exit succesfull");
	}

}
