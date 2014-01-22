package impl;

import interfaces.Contact;
import java.io.Serializable;

/**
 * A contact is a person we are making business with or may do in the future. 
 *
 * Contacts have an ID (unique), a name (probably unique, but maybe
 * not), and notes that the user may want to save about them.
 */
public class ContactImpl implements Contact, Serializable {

	private static final long serialVersionUID = 217761466628658229L;
	private int id;
	private String name;
	private String note;
	
	/**
	 * Constructor for creating a new contact.
	 * @param id int. The ID of the contact.
	 * @param name String. The name of the contact.
	 * @param note String. The note for the contact.
	 */
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

	/**
	 * Returns our notes about the contact, if any. 
	 *
	 * If we have not written anything about the contact, the empty
	 * string is returned. *
	 * @return a string with notes about the contact, maybe empty. 
	 */
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