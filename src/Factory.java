import utilities.ManagerData;
import utilities.ManagerFileIO;
import impl.ContactManagerImpl;

public class Factory {
	
	public static Menu build() {
		return new Menu(new Manager(new ContactManagerImpl(new ManagerFileIO().load(new ManagerData()))));
	}
	
	

}
