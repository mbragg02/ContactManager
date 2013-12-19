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



	// Contacts methods

	@Override
	public void addNewContact(String name, String notes) {

		Contact newComtact = new ContactImpl(contactId, name, notes);

		contacts.put(contactId, newComtact);
		contactsMeetings.put(newComtact, new HashSet<Meeting>());
		contactsPastMeetings.put(newComtact, new HashSet<PastMeeting>());
		++contactId;
	}

	@Override
	public Set<Contact> getContacts(int... ids) {

		Set<Contact> result = new HashSet<Contact>();

		for (int id : ids) {
			Contact contact = contacts.get(id);
			System.out.println(id);
			// TODO check for null
			result.add(contact);

		}
		return result;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		Set<Contact> result = new HashSet<Contact>();

		for (Contact contact : contacts.values()) {
			if (contact.getName().contains(name)) {
				result.add(contact);
			}
		}


		return result;
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

			// add meeting to the future meetings Map
			futureMeetings.put(meetingId, (FutureMeeting) newFutureMeeting);


			// add meeting to the matching contacts
			for (Contact contact : newFutureMeeting.getContacts()) {
				contactsMeetings.get(contact).add(newFutureMeeting);
			}

			// add meeting to the date/meeting Map
			Set<Meeting> meetingsOnDate = meetingDates.get(newFutureMeeting.getDate());

			if (meetingsOnDate == null) {
				meetingsOnDate = new HashSet<>();
				meetingDates.put(newFutureMeeting.getDate(), meetingsOnDate);
			}

			meetingsOnDate.add(newFutureMeeting);


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

			pastMeetings.put(meetingId, (PastMeeting) newPastMeeting);

			for (Contact contact : newPastMeeting.getContacts()) {
				contactsPastMeetings.get(contact).add((PastMeeting) newPastMeeting);
			}

			Set<Meeting> meetingsOnDate = meetingDates.get(newPastMeeting.getDate());

			if (meetingsOnDate == null) {
				meetingsOnDate = new HashSet<>();
				meetingDates.put(newPastMeeting.getDate(), meetingsOnDate);
			}

			meetingsOnDate.add(newPastMeeting);

			System.out.println("Meeting on " + date.getTime() + " added");

			meetingId ++;
		}
	}


	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO
	}



	// Meetings getters

	@Override
	public PastMeeting getPastMeeting(int id) {

		// TODO check ids are valid past meeting ids

		PastMeeting result = pastMeetings.get(id);
		return result;
	}



	@Override
	public FutureMeeting getFutureMeeting(int id) {

		// TODO check ids are valid future meeting ids

		FutureMeeting result = futureMeetings.get(id);

		return result;
	}



	@Override
	public Meeting getMeeting(int id) {

		// TODO check for exceptions

		Meeting meeting = pastMeetings.get(id);

		if (meeting == null) {
			return futureMeetings.get(id);
		} else {
			return meeting;
		}

	}


	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {

		//TODO check date is valid / not null

		Set<Meeting> meetings = meetingDates.get(date);
		if (meetings == null) {
			meetings = new HashSet<Meeting>();
		}

		return new LinkedList<Meeting>(meetings);
	}



	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {


		// TODO check incoming contact is valid / not null

		List<Meeting> result = new LinkedList<Meeting>(contactsMeetings.get(contact));

		return result;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {


		// TODO check incoming contact is valid

		List<PastMeeting> result = new LinkedList<PastMeeting>(contactsPastMeetings.get(contact));


		return result;
	}


}
