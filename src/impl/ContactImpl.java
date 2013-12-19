package impl;

import interfaces.Contact;

import java.io.Serializable;



public class ContactImpl implements Contact, Serializable {

	private static final long serialVersionUID = 217761466628658229L;
	private int id;
	private String name;
	private String note;
	
	public ContactImpl(int id, String name, String note) {
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
	

}
