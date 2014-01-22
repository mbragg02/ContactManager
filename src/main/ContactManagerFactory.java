package main;

import utilities.ManagerData;
import utilities.ManagerFileIO;
import impl.ContactManagerImpl;
import interfaces.ContactManager;

/**
 * Factory class to build all new objects for Contact Manager and tests
 * @author Michael Bragg
 */
public class ContactManagerFactory {
	
	private static ContactManagerFactory instance = null;
	private Menu build;
	
	private ContactManagerFactory() {
		build = newMenu();	
	}
	
	static {
		instance = new ContactManagerFactory();
	}
	
	public static ContactManagerFactory getInstance() {
		return instance;
	}
	
	public Menu getBuild() {
		return this.build;
	}
	
	private Menu newMenu() {
		return new Menu(newManager());
	}
	private Manager newManager() {
		return new Manager(newContactManager());
	}
	
	private ContactManager newContactManager() {
		return new ContactManagerImpl(newDataWithPersistance());
	}
	private ManagerData newDataWithPersistance() {
		return new ManagerFileIO().load(newData());
	}
	private ManagerData newData() {
		return new ManagerData();
	}
}