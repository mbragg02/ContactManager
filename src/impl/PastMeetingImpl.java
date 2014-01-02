package impl;

import interfaces.Contact;
import interfaces.PastMeeting;
import java.util.Calendar;
import java.util.Set;

/**
 * A meeting that was held in the past. 
 *
 * It includes your notes about what happened and what was agreed. 
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting  {

	private static final long serialVersionUID = -5674794585117543367L;
	private String notes; 

	/**
	 * Constructor for new Past Meeting.
	 * @param id int. ID for the meeting
	 * @param date Calendar. The date for the meeting.
	 * @param members Set<Contact>. The list of contacts attending the meeting.
	 * @param notes String. The notes for the meeting.
	 */
	public PastMeetingImpl(int id, Calendar date, Set<Contact> members, String notes) {
		super(id, date, members);
		setNotes(notes);
	}

	private void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Returns the notes from the meeting. 
	 *
	 * If there are no notes, the empty string is returned. 
	 *
	 * @return String. The notes from the meeting. 
	 */
	@Override
	public String getNotes() {
		String result;
		if (this.notes == null) {
			result = "";
		} else {
			result = this.notes;
		}
		return result;
	}

}
