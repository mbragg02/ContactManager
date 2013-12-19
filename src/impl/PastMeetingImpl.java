package impl;
import interfaces.Contact;
import interfaces.PastMeeting;

import java.util.Calendar;
import java.util.Set;


public class PastMeetingImpl extends MeetingImpl implements PastMeeting  {
	
	private static final long serialVersionUID = -5674794585117543367L;
	private String notes; 
	
	public PastMeetingImpl(int id, Calendar date, Set<Contact> members, String notes) {
		super(id, date, members);
		setNotes(notes);
		
	}
	
	private void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String getNotes() {
		return this.notes;
	}

}
