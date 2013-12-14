import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactManagerImpl implements ContactManager {
	
	private int contactId;
	private int meetingId;
	private List<Contact> contactList;
	private List<Meeting> meetingList;
	
	public ContactManagerImpl() {
		contactId = 0;
		meetingId = 0; 
		contactList = new ArrayList<>();
		meetingList = new ArrayList<>();
	}
	
	// Contacts methods
	
	@Override
	public void addNewContact(String name, String notes) throws NullPointerException {

		Contact newComtact = new ContactImpl(contactId, name, notes);
		contactList.add(newComtact);
		contactId ++ ;
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		
		Set<Contact> result = new HashSet<Contact>();
		
		for (int id : ids) {
			Contact aContact = contactList.get(id);
			// check for null
			result.add(aContact);

		}
		return result;
	}

	@Override
	public Set<Contact> getContacts(String name) throws NullPointerException {
		Set<Contact> result = new HashSet<Contact>();
		int contactsNo = contactList.size();
		
		for (int i = 0; i < contactsNo; i ++) {
			if(contactList.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
				result.add(contactList.get(i));
			}
		}
		return result;
	}
	
	
	
	// Meetings setters

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub
		
	}
	
	// Meetings getters

	@Override
	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}



	
	
	
	/**
	 * Save all data to disk. 
	 * 
	 * This method must be executed when the program is
	 * closed and when/if the user requests it. 
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}
 
}
