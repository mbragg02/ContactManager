import java.util.List;


public class ContactImpl implements Contact {

	private int id;
	private String name;
	private String note;
	private List<Meeting> meetings;
	
	public ContactImpl(int id, String name, String note) {
		setId(id);
		setName(name);
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
		return this.note;
	}

	@Override
	public void addNotes(String note) {
		this.note = note;
	}
	
	@Override
	public void addMeeting(Meeting meeting) {
		this.meetings.add(meeting);
	}

}
