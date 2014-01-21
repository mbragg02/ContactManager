package main;
import utilities.ManagerData;
import utilities.ManagerFileIO;
import impl.ContactManagerImpl;
import interfaces.ContactManager;

/**
 * Class with static methods to return new objects for Contact Manager and tests
 * @author Michael Bragg
 */
public class Factory {
	
	public static Menu build() {
		return newMenu();
	}
	
	private static Menu newMenu() {
		return new Menu(newManager());
	}
	private static Manager newManager() {
		return new Manager(newContactManager());
	}
	
	private static ContactManager newContactManager() {
		return new ContactManagerImpl(newFileIO());
	}
	private static ManagerData newFileIO() {
		return new ManagerFileIO().load(newData());
	}
	private static ManagerData newData() {
		return new ManagerData();
	}
	
	public static ContactManager buildTestEnviroment() {
		return new ContactManagerImpl(new ManagerData());
	}
	
	
	

}
