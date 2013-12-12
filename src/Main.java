import java.util.Set;


public class Main {

	public static void main(String[] args) {
		Main launcher = new Main();
		launcher.launch();

	}
	
	public void launch() {
		ContactManager manager = new ContactManagerImpl();
		manager.addNewContact("Michael", "this is a note");
		manager.addNewContact("Michael123", "this123 is a note");
		Set<Contact> results = manager.getContacts("michael");
		
		for (Contact x : results) {
			System.out.println(x.getId());
		}
		
	}

}
