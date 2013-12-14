import java.util.Calendar;
import java.util.Set;


public class MeetingImpl implements Meeting {
	
	private int id;
	private Calendar date;
	private Set<Contact> members;
	
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

	@Override
	public Set<Contact> getContacts() {
		return members;
	}

}
