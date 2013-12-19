import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactManagerImpl implements ContactManager {

	private static final String FILENAME = "contacts.txt";

	private int contactId;
	private int meetingId;

	// Contains the contacts 
	private Map<Integer, Contact> contacts = new HashMap<>();

	// Contains the meetings and their ids
	private Map<Integer, PastMeeting> pastMeetings = new HashMap<>();
	private Map<Integer, FutureMeeting> futureMeetings = new HashMap<>();

	// Contains the contacts meetings
	private Map<Contact, Set<Meeting>> contactsMeetings = new HashMap<>() ;
	private Map<Contact, Set<PastMeeting>> contactsPastMeetings = new HashMap<>();

	// Contains dates and meetings scheduled for those dates
	private Map<Calendar, Set<Meeting>> meetingDates = new HashMap<>();

	private Calendar calendar = new GregorianCalendar(); 

	public ContactManagerImpl() {
		load();
		// TODO CHECK FUTURE AND PAST MEETINGS are in correct lists

	}


	@SuppressWarnings("unchecked")
	private void load() {
		System.out.println("Loading data...");

		// file does not exist or is directory or isn't readable
		if (!new File(FILENAME).exists()) {
			contactId = 0;
			meetingId = 0; 
		} else
			try (ObjectInputStream
					d = new ObjectInputStream(
							new BufferedInputStream(
									new FileInputStream(FILENAME)));) {
				contacts = (Map<Integer, Contact>) d.readObject();
				pastMeetings = (Map<Integer, PastMeeting>) d.readObject();
				futureMeetings = (Map<Integer, FutureMeeting>) d.readObject();
				contactsMeetings = (Map<Contact, Set<Meeting>>) d.readObject();
				contactsPastMeetings = (Map<Contact, Set<PastMeeting>>) d.readObject();
				meetingDates = (Map<Calendar, Set<Meeting>>) d.readObject();
				contactId = contacts.size();
				meetingId = Math.max(futureMeetings.size(), pastMeetings.size());

			} catch (IOException | ClassNotFoundException ex) {
				System.err.println("On read error " + ex);
			}


	}

	/**
	 * Save all data to disk. 
	 * 
	 * This method must be executed when the program is
	 * closed and when/if the user requests it. 
	 */
	@Override
	public void flush() {
		System.out.println("Saving data...");
		try (ObjectOutputStream encode = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILENAME)));) {
			encode.writeObject(contacts);
			encode.writeObject(pastMeetings);
			encode.writeObject(futureMeetings);
			encode.writeObject(contactsMeetings);
			encode.writeObject(contactsPastMeetings);
			encode.writeObject(meetingDates);
		} catch (IOException ex) {
			System.err.println("write error: " + ex);
		}
	}



	// CONTACTS

	@Override
	public void addNewContact(String name, String notes) {

		if (name == null) {
			throw new NullPointerException("Name is null.");
		}
		if (notes == null) {
			throw new NullPointerException("Note is null.");
		}

		// Create a new contact 
		Contact newComtact = new ContactImpl(contactId, name, notes);

		// Add the new Contact to the contacts list
		contacts.put(contactId, newComtact);

		// Create new empty lists to hold the contacts past & future meetings
		contactsMeetings.put(newComtact, new HashSet<Meeting>());
		contactsPastMeetings.put(newComtact, new HashSet<PastMeeting>());

		++contactId;
	}

	@Override
	public Set<Contact> getContacts(int... ids) {

		Set<Contact> result = new HashSet<Contact>();

		for (int id : ids) {
			Contact contact = contacts.get(id);

			if (contact == null) {
				throw new IllegalArgumentException("Could not find a contact with the id: " + id);
			}
			result.add(contact);

		}
		return result;
	}

	@Override
	public Set<Contact> getContacts(String name) {

		if (name == null) {
			throw new NullPointerException("Name is null");
		}

		Set<Contact> result = new HashSet<Contact>();

		for (Contact contact : contacts.values()) {
			if (contact.getName().contains(name)) {
				result.add(contact);
			}
		}


		return result;
	}

	private void checkContactIsValid(Contact contact) {
		if (contact == null) {
			throw new NullPointerException("Contact is null.");
		}
		if (!contacts.containsValue(contact)) {
			throw new IllegalArgumentException(contact.getName() + " is unknown");
		}
	}

	private void checkContactsAreValid(Set<Contact> contacts) {
		if(contacts.isEmpty()) {
			throw new IllegalArgumentException("No contacts supplied.");
		}
		for (Contact c : contacts) {
			if (!this.contacts.containsValue(c)) {
				throw new IllegalArgumentException(c.getName() + " not found");
			}
		}

	}




	// MEETING SETTERS

	private void addMeeting(Meeting meeting) {

		if (meeting instanceof FutureMeeting) {
			// add meeting to the future meetings Map
			futureMeetings.put(meetingId, (FutureMeeting) meeting);

			// add meeting to the matching contacts
			for (Contact contact : meeting.getContacts()) {
				contactsMeetings.get(contact).add(meeting);
			}
		}
		if (meeting instanceof PastMeeting) {
			pastMeetings.put(meetingId, (PastMeeting) meeting);

			for (Contact contact : meeting.getContacts()) {
				contactsPastMeetings.get(contact).add((PastMeeting) meeting);
			}
		}

		// add meeting to the date/meeting Map
		Set<Meeting> meetingsOnDate = meetingDates.get(meeting.getDate());

		if (meetingsOnDate == null) {
			meetingsOnDate = new HashSet<>();
			meetingDates.put(meeting.getDate(), meetingsOnDate);
		}

		meetingsOnDate.add(meeting);

		meetingId++;
	}

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		Meeting newFutureMeeting = null;
		int result = 0;

		if (date.getTime().before(calendar.getTime())) {
			throw new IllegalArgumentException("Date must be in the future.");
		} else {

			checkContactsAreValid(contacts);

			newFutureMeeting = new FutureMeetingImpl(meetingId, date, contacts);

			addMeeting(newFutureMeeting);

			result = newFutureMeeting.getId();
			System.out.println("Your new meeting has been added with id: " + result);

		}

		return result;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {

		if (contacts == null | date == null | text == null) {
			throw new NullPointerException("Contacts, date or note can not be null");
		}

		if (contacts.isEmpty()) {
			throw new IllegalArgumentException("List of contacts is empty");
		}

		checkContactsAreValid(contacts);

		if (date.getTime().after(calendar.getTime())) {
			System.out.println("date/time entered was in the futute. A past meetings date must be in the past.");
		} else {

			Meeting newPastMeeting = new PastMeetingImpl(meetingId, date, contacts, text);
			addMeeting(newPastMeeting);
			System.out.println("Your new meeting has been added with id: " + newPastMeeting.getId());
		}
	}


	@Override
	public void addMeetingNotes(int id, String text) {
		if (text == null) {
			throw new NullPointerException("Notes text is null.");
		}

		if (pastMeetings.containsKey(id)) {
			//past meeting 
		} else if (futureMeetings.containsKey(id)) {
			//future meeting
		} else {
			throw new IllegalArgumentException("Could not find any meetings with the id of: " + id);
		}

	}



	// Meetings getters

	@Override
	public PastMeeting getPastMeeting(int id) {			
		PastMeeting meeting = pastMeetings.get(id);
		if (meeting == null) {
			return null;
		}

		if(futureMeetings.containsKey(id)) {
			throw new IllegalArgumentException("There is a meeting with that ID in the future");
		}

		return meeting;
	}



	@Override
	public FutureMeeting getFutureMeeting(int id) {
		FutureMeeting meeting = futureMeetings.get(id);

		if (meeting == null) {
			return null;
		}

		if(pastMeetings.containsKey(id)) {
			throw new IllegalArgumentException("There is a meeting with that ID in the past");
		}

		return meeting;
	}



	@Override
	public Meeting getMeeting(int id) {

		Meeting meeting = pastMeetings.get(id);

		if (meeting == null) {
			return futureMeetings.get(id);
		} else {
			return meeting;
		}

	}


	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {

		Set<Meeting> meetings = meetingDates.get(date);
		if (meetings == null) {
			meetings = new HashSet<Meeting>();
		}

		return new LinkedList<Meeting>(meetings);
	}


	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {

		checkContactIsValid(contact);

		List<Meeting> result = new LinkedList<Meeting>(contactsMeetings.get(contact));

		return result;
	}

	
	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {

		checkContactIsValid(contact);

		List<PastMeeting> result = new LinkedList<PastMeeting>(contactsPastMeetings.get(contact));

		return result;
	}


}
