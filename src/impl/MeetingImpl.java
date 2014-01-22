package impl;
import interfaces.Contact;
import interfaces.Meeting;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings 
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts 
 */
public class MeetingImpl implements Meeting, Serializable {

	private static final long serialVersionUID = -8414909587892809884L;
	private int id;
	private Calendar date;
	private Set<Contact> members;

	/**
	 * Constructor for new Meeting.
	 * @param id int. The ID for the meeting.
	 * @param date Calendar. The date for the meeting.
	 * @param members Set<Contact>. The list of contacts attending the meeting.
	 */
	public MeetingImpl(int id, Calendar date, Set<Contact> members) {
		setId(id);
		setDate(date);
		setMemebers(members);
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setDate(Calendar date) {
		this.date = date;
	}

	private void setMemebers(Set<Contact> members) {
		this.members = members;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public Calendar getDate() {
		return this.date;
	}


	/**
	 * Return the details of people that attended the meeting. 
	 *
	 * The list contains a minimum of one contact (if there were
	 * just two people: the user and the contact) and may contain an
	 * arbitrary number of them. 
	 *
	 * @return Set<Contact>. A list of people that attended the meeting. 
	 */
	@Override
	public Set<Contact> getContacts() {
		return members;
	}

}
