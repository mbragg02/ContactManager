import main.ContactManagerFactory;

/**
 * Contacts Manager Application v1.1
 * PIJ 2013/2014
 * @author Michael Bragg
 */
public class Launcher {

	public static void main(String[] args) {
		ContactManagerFactory.getInstance().getBuild().launchMenu();
	}
}