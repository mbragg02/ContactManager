package impl;
import interfaces.Contact;
import interfaces.ContactManager;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private Map<Contact, Set<Meeting>> contactsFutureMeetings = new HashMap<>() ;
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
				contactsFutureMeetings = (Map<Contact, Set<Meeting>>) d.readObject();
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
			encode.writeObject(contactsFutureMeetings);
			encode.writeObject(contactsPastMeetings);
			encode.writeObject(meetingDates);
		} catch (IOException ex) {
			System.err.println("write error: " + ex);
		}
	}



	// CONTACTS
	/**
	 * Create a new contact with the specified name and notes.
	 * 
	 * @param name the name of the contact.
	 * @param notes notes to be added about the contact
	 * @throws NullPointerException if the name or the notes are null.
	 */
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
		contactsFutureMeetings.put(newComtact, new HashSet<Meeting>());
		contactsPastMeetings.put(newComtact, new HashSet<PastMeeting>());

		++contactId;
	}

	/**
	 * Returns a list with the contacts whose name contains that string. 
	 *
	 * @param name the string to search for
	 * @return a list with the contacts whose name contains that string.
	 * @throws NullPointerException if the parameter is null 
	 */
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

	/**
	 * Returns a list containing the contacts that correspond to the IDs
	 *  
	 * @param ids an arbitrary number of contact IDs
	 * @return a list containing the contacts that correspond to the IDs.
	 * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	 */
	@Override
	public Set<Contact> getContacts(int... ids) {
		

		if (ids == null || ids.length == 0) {
			throw new NullPointerException("null argument");
		}

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





	/**
	 * Checks if a supplied contact is not null and the contact exists in the application.
	 * @param contact Contact
	 * @throws NullPointerException if the supplied contact is null
	 * @throws IllegalArgumentException if the contact does not exist.
	 */
	private void checkContactIsValid(Contact contact) {
		if (contact == null) {
			throw new NullPointerException("Contact is null.");
		}
		if (!contacts.containsValue(contact)) {
			throw new IllegalArgumentException(contact.getName() + " does not exists");
		}
	}

	/**
	 * Checks if a supplied set of contacts are valid.
	 * @param contacts Set<Contact>
	 * @throws IllegalArgumentException if the supplied set is empty
	 * @throws IllegalArgumentException if a contact does not exist.
	 */
	private void checkContactsAreValid(Set<Contact> contacts) {
		if(contacts.isEmpty()) {
			throw new NullPointerException("No contacts supplied.");
		}
		for (Contact c : contacts) {
			if (!this.contacts.containsValue(c)) {
				throw new IllegalArgumentException(c.getName() + " does not exists");
			}
		}

	}





	// Meeting Setters

	/**
	 * Method to add a new meeting
	 * Checks if the Meeting is a FutureMeeting or PastMeeting.
	 * Adds meeting to the appropriate data structures: futureMeeting, pastMeeting, contactsFutureMeeting, ContactsPastMeeting, meetingDates
	 * 
	 * @param meeting Meeting
	 */
	private void addMeeting(Meeting meeting) {

		if (meeting instanceof FutureMeeting) {
			// add meeting to the future meetings Map
			futureMeetings.put(meeting.getId(), (FutureMeeting) meeting);

			// add meeting to the matching contacts
			for (Contact contact : meeting.getContacts()) {
				contactsFutureMeetings.get(contact).add(meeting);
			}
		}
		if (meeting instanceof PastMeeting) {
			pastMeetings.put(meeting.getId(), (PastMeeting) meeting);

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


	}



	/**
	 * Add a new meeting to be held in the future. 
	 *
	 * @param contacts a list of contacts that will participate in the meeting
	 * @param date the date on which the meeting will take place
	 * @return the ID for the meeting
	 * @throws IllegalArgumentException if the meeting is set for a time in the past,
	 *
	 */
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
		meetingId++;
		return result;
	}


	/**
	 * Create a new record for a meeting that took place in the past. 
	 *
	 * @param contacts a list of participants
	 * @param date the date on which the meeting took place
	 * @param text messages to be added about the meeting.
	 * @throws IllegalArgumentException if the list of contacts is empty, or any of the contacts does not exist
	 * @throws NullPointerException if any of the arguments is null 
	 */
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
			throw new IllegalArgumentException("Date must be in the past");
		} else {

			Meeting newPastMeeting = new PastMeetingImpl(meetingId, date, contacts, text);
			addMeeting(newPastMeeting);
			System.out.println("Your new meeting has been added with id: " + newPastMeeting.getId());
		}
		meetingId++;
	}



	/**
	 * Add notes to a meeting. 
	 *
	 * This method is used when a future meeting takes place, and is
	 * then converted to a past meeting (with notes). 
	 *
	 * It can be also used to add notes to a past meeting at a later date. 
	 *
	 * @param id the ID of the meeting
	 * @param text messages to be added about the meeting.
	 * @throws IllegalArgumentException if the meeting does not exist
	 * @throws IllegalStateException if the meeting is set for a date in the future
	 * @throws NullPointerException if the notes are null 
	 */
	@Override
	public void addMeetingNotes(int id, String text) {
		if (text == null) {
			throw new NullPointerException("Notes text is null.");
		}

		if (pastMeetings.containsKey(id)) {
			//past meeting 
			PastMeeting meeting = pastMeetings.remove(id);
			String notes = meeting.getNotes() + '\n' + text;
			updateMeeting(meeting, notes);

		} else if (futureMeetings.containsKey(id)) {

			//future meeting
			FutureMeeting meeting = futureMeetings.get(id);

			// Update the eeting to a past meeting
			updateMeeting(meeting, text);

			// finally remove the original future meeting
			futureMeetings.remove(id);
		} else {
			throw new IllegalArgumentException("Could not find any meetings with the id of: " + id);
		}

	}


	private void updateMeeting(Meeting meeting, String text) {
		if (meeting.getDate().getTime().before(calendar.getTime())) {
			// meeting has passed

			//Create a new past meeting
			Meeting newPastMeeting = new PastMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts(), text);


			// remove the old meeting from the future meeting data structures
			meetingDates.get(meeting.getDate()).remove(meeting);


			if (meeting instanceof PastMeeting) {

				for (Contact contact : meeting.getContacts()) {
					contactsPastMeetings.get(contact).remove(meeting);
				}

			} else if (meeting instanceof FutureMeeting) {

				for (Contact contact : meeting.getContacts()) {
					contactsFutureMeetings.get(contact).remove(meeting);
				}

			}

			// Add the new post meeting to the correct data structures
			addMeeting(newPastMeeting);
		} else {
			throw new IllegalArgumentException("Meeting is in the future");
		}
	}










	// Meetings getters
	/**
	 * Returns the PAST meeting with the requested ID, or null if it there is none. 
	 *
	 * @param id the ID for the meeting
	 * @return the meeting with the requested ID, or null if it there is none.
	 * @throws IllegalArgumentException if there is a meeting with that ID happening in the future
	 */
	@Override
	public PastMeeting getPastMeeting(int id) {	
		
		if(futureMeetings.containsKey(id)) {
			throw new IllegalArgumentException("There is a meeting with that ID in the future");
		}
		
		PastMeeting meeting = pastMeetings.get(id);
		if (meeting == null) {
			return null;
		}

		return meeting;
	}


	/**
	 * Returns the FUTURE meeting with the requested ID, or null if there is none. 
	 *
	 * @param id the ID for the meeting
	 * @return the meeting with the requested ID, or null if it there is none.
	 * @throws IllegalArgumentException if there is a meeting with that ID happening in the past 
	 */
	@Override
	public FutureMeeting getFutureMeeting(int id) {
		
		if(pastMeetings.containsKey(id)) {
			throw new IllegalArgumentException("There is a meeting with that ID in the past");
		}
		
		FutureMeeting meeting = futureMeetings.get(id);

		if (meeting == null) {
			return null;
		}

		return meeting;
	}


	/**
	 * Returns the meeting with the requested ID, or null if there is none.
	 * 
	 * @param id the ID for the meeting
	 * @return the meeting with the requested ID, or null if there is none.
	 */
	@Override
	public Meeting getMeeting(int id) {

		Meeting meeting = pastMeetings.get(id);

		if (meeting == null) {
			return futureMeetings.get(id);
		} else {
			return meeting;
		}

	}

	/**
	 * Returns the list of meetings that are scheduled for, or that took
	 * place on, the specified date 
	 *
	 * If there are none, the returned list will be empty. Otherwise,
	 * the list will be chronologically sorted and will not contain any 
	 * duplicates.
	 *
	 * @param date the date
	 * @return the list of meetings 
	 */
	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {

		Set<Meeting> meetings = meetingDates.get(date);
		if (meetings == null) {
			meetings = new HashSet<Meeting>();
		}

		return new LinkedList<Meeting>(meetings);
	}

	/**
	 * Returns the list of future meetings scheduled with this contact. 
	 *
	 * If there are none, the returned list will be empty. Otherwise,
	 * the list will be chronologically sorted and will not contain any 
	 * duplicates.
	 *
	 * @param contact one of the users contacts
	 * @return the list of future meeting(s) scheduled with this contact
	 * @throws IllegalArgumentException if the contact does not exist 
	 */
	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {

		checkContactIsValid(contact);

		List<Meeting> result = new LinkedList<Meeting>(contactsFutureMeetings.get(contact));

		return result;
	}



	/**
	 * Returns the list of past meetings in which this contact has participated. 
	 *
	 * If there are none, the returned list will be empty. Otherwise,
	 * the list will be chronologically sorted and will not contain any 
	 * duplicates.
	 *
	 * @param contact one of the users contacts
	 * @return the list of future meeting(s) scheduled with this contact (maybe empty).
	 * @throws IllegalArgumentException if the contact does not exist
	 */
	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {

		checkContactIsValid(contact);

		List<PastMeeting> result = new LinkedList<PastMeeting>(contactsPastMeetings.get(contact));

		return result;
	}


}
