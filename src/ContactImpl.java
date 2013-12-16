import java.util.ArrayList;
import java.util.List;


public class ContactImpl implements Contact {

	private int id;
	private String name;
	private String note;
	private List<Meeting> meetings;
	
	public ContactImpl(int id, String name, String note) {
		meetings = new ArrayList<Meeting>();
		setId(id);
		setName(name);
		addNotes(note);

	}
	
	private void setId(int id) {
		this.id = id;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNotes() {
		String result;
		if (this.note == null) {
			result = "";
		} else {
			result = this.note;
		}
		return result;
	}

	@Override
	public void addNotes(String note) {
		this.note = note;
	}
	
	@Override
	public void addMeeting(Meeting meeting) {
		
		this.meetings.add(meeting);
	}
	
	public List<Meeting> getMeetings() {
		return this.meetings;
	}

}
