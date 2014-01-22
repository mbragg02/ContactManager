package utilities;

import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class to store all the data created in Contact Manager
 * @author Michael Bragg
 * Contains getters, adders and increment methods.
 */
public class ManagerData implements Serializable {

	private static final long serialVersionUID = 5186539026664203080L;
	private int contactId;
	private int meetingId;

	// Map from Contact IDs to Contacts
	private Map<Integer, Contact> contacts = new HashMap<Integer, Contact>();

	// Map from meeting IDs to Meetings
	private Map<Integer, PastMeeting> pastMeetings = new HashMap<Integer, PastMeeting>();
	private Map<Integer, FutureMeeting> futureMeetings = new HashMap<Integer, FutureMeeting>();

	// Maps from a Contact to Sets of past/future Meetings 
	private Map<Contact, Set<Meeting>> contactsFutureMeetings = new HashMap<Contact, Set<Meeting>>() ;
	private Map<Contact, Set<PastMeeting>> contactsPastMeetings = new HashMap<Contact, Set<PastMeeting>>();

	// Maps from Calendar dates to Sets of Meetings
	private Map<Calendar, Set<Meeting>> meetingDates = new TreeMap<Calendar, Set<Meeting>>();
	
	public ManagerData() {
		setContactId(0);
		setMeetingId(0);
	}
	
	// Getters and setters for data structures
	
	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public void incrementContactId() {
		++contactId;
	}
	public void incrementMeetingId() {
		++meetingId;
	}

	public int getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}

	public Map<Integer, Contact> getContacts() {
		return contacts;
	}

	public Map<Integer, PastMeeting> getPastMeetings() {
		return pastMeetings;
	}

	public void addPastMeeting(int meetingId, PastMeeting pastMeeting) {
		this.pastMeetings.put(meetingId, pastMeeting);
	}

	public Map<Integer, FutureMeeting> getFutureMeetings() {
		return futureMeetings;
	}

	public void addFutureMeeting(int meetingID, FutureMeeting futureMeeting) {
		this.futureMeetings.put(meetingID, futureMeeting);
	}

	public Map<Contact, Set<Meeting>> getContactsFutureMeetings() {
		return contactsFutureMeetings;
	}

	public Map<Contact, Set<PastMeeting>> getContactsPastMeetings() {
		return contactsPastMeetings;
	}

	public void addMeetingDate(Calendar calendar, Set<Meeting> meetings) {
		this.meetingDates.put(calendar, meetings);

	}

	public Map<Calendar, Set<Meeting>> getMeetingDates() {
		return meetingDates;
	}
	
	
}
