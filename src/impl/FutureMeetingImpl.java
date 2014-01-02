package impl;
import interfaces.Contact;
import interfaces.FutureMeeting;

import java.util.Calendar;
import java.util.Set;

/**
 * A meeting to be held in the future 
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	private static final long serialVersionUID = -2310318760334810131L;
	
	/**
	 * Constructor for a new Future Meeting.
	 * @param id int. The ID of the meeeting.
	 * @param date Calendar. YThe date for the meeting.
	 * @param members Set<Contact>. The list of contacts attending the meeting.
	 */
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> members) {
		super(id, date, members);
	}

}
