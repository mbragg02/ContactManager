import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactManagerImpl implements ContactManager {

	private int contactId;
	private int meetingId;
	private List<Contact> contactList;
	private List<Meeting> meetingList;
	private List<PastMeeting> pastMeetingList;
	private Calendar calendar = new GregorianCalendar(); 

	public ContactManagerImpl() {
		contactId = 0;
		meetingId = 0; 
		contactList = new ArrayList<>();
		meetingList = new ArrayList<>();
		pastMeetingList = new ArrayList<>();
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

	@Override
	public List<Contact> getContacts() {
		return contactList;
	}



	// Meetings setters

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		Meeting newFutureMeeting = null;
		int result = 0;
		if (date.getTime().before(calendar.getTime())) {
			System.out.println("date/time entered was in the past. A future meetings date must be in the future.");
		} else {
			newFutureMeeting = new FutureMeetingImpl(meetingId, date, contacts);
			meetingList.add(newFutureMeeting);
			meetingId++;
			result = newFutureMeeting.getId();
			System.out.println("Your new meeting has been added with id: " + result);

		}

		return result;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		if (date.getTime().after(calendar.getTime())) {
			System.out.println("date/time entered was in the futute. A past meetings date must be in the past.");
		} else {
			Meeting newPastMeeting = new PastMeetingImpl(meetingId, date, contacts, text);
			meetingList.add(newPastMeeting);
			pastMeetingList.add((PastMeeting) newPastMeeting);
			addMeetingToContacts(contacts, newPastMeeting);
			System.out.println("Meeting on " + date.getTime() + " added");

			meetingId ++;
		}
	}

	private void addMeetingToContacts(Set<Contact> contacts, Meeting meeting) {
		for (Contact x : contacts) {
			x.addMeeting(meeting);
		}
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		pastMeetingList.get(id).setNotes(text);
	}



	// Meetings getters

	@Override
	public PastMeeting getPastMeeting(int id) {
		PastMeeting result = null;

		for (int i = 0; i < pastMeetingList.size(); i ++ ) {

			if (pastMeetingList.get(i).getId() == id) {
				result = pastMeetingList.get(i);
			}
		}

		if (result != null) {
			System.out.println("Pastmeeting id = " + result.getId());
		} else {
			System.out.println("No meeting found with the id: " + id);
		}
		return result;
	}



	@Override
	public FutureMeeting getFutureMeeting(int id) {
		FutureMeeting result = (FutureMeeting) getMeeting(id);

		if (result.getDate().getTime().before(calendar.getTime())) {
			System.out.println("The meeting with the id " + id + " is in past");
			return null;
		} else {
			System.out.println("Meeting " + result.getId() + " returned.");
			return result;
		}
	}



	@Override
	public Meeting getMeeting(int id) {
		Meeting result = null;

		for (int i = 0; i < meetingList.size(); i ++) {
			if(meetingList.get(i).getId() == id) {
				result = meetingList.get(i);
			}
		}
		if (result != null) {
			System.out.println("Meeting found.");
		} else {
			System.out.println("No meeting found with the id: " + id);
		}
		return result;
	}





	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> result = new ArrayList<Meeting>();

		for (int i = 0; i < meetingList.size(); i ++) {
			if (meetingList.get(i).getDate().equals(date)) {
				result.add(meetingList.get(i));
			}
		}	
		return result;
	}



	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> meetings = null;
		List<Meeting> result = null;
		try {
			meetings = getAllMeetings(contact);
		} catch (IllegalArgumentException ex) {
			System.out.println("Contact does not exist");
            ex.printStackTrace();
		}
		
		if (meetings != null) {
			for (int i = 0; i < meetings.size(); i ++) {
				if(meetings.get(i).getDate().getTime().before(calendar.getTime())) {
					meetings.remove(i);
				}
			}
		}
		if (meetings == null) {
			System.out.println("No future meetings to display."); 
			return null;
		} else {
			result = new ArrayList<Meeting>();
			
			for (Meeting x : meetings) {
				result.add((FutureMeeting) x);
			}
		}
		
		
		return result;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		List<Meeting> meetings = null;
		try {
			meetings = getAllMeetings(contact);
		} catch (IllegalArgumentException ex) {
			System.out.println("Contact does not exist");
            ex.printStackTrace();
		}
		
		List<PastMeeting> result = new ArrayList<PastMeeting>();
		
		if (meetings != null) {
			for (int i = 0; i < meetings.size(); i ++) {
				if (meetings.get(i).getDate().getTime().after(calendar.getTime())) {
					meetings.remove(i);
				}
			}
		}
		if (meetings == null) {
			System.out.println(contact + " has no past meetings to display.");
		} else {
			for (Meeting x : meetings) {
				result.add((PastMeeting) x);
			}
		}
		
		return result;
	}

	private List<Meeting> getAllMeetings(Contact contact) {
		Contact tempContact = contact;
		List<Meeting> meetings = null;

		if (tempContact.getMeetings() != null) {
			meetings = tempContact.getMeetings();
		} else {
			System.out.println("This user has no meetings stored.");
		}
		return meetings;
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
