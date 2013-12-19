package impl;
import interfaces.Contact;
import interfaces.FutureMeeting;

import java.util.Calendar;
import java.util.Set;


public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	private static final long serialVersionUID = -2310318760334810131L;

	public FutureMeetingImpl(int id, Calendar date, Set<Contact> members) {
		super(id, date, members);
	}

}
