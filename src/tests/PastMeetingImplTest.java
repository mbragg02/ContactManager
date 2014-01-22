package tests;

import static org.junit.Assert.*;
import java.util.*;
import impl.ContactImpl;
import impl.PastMeetingImpl;
import interfaces.Contact;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

public class PastMeetingImplTest {

	private PastMeeting test;
	private int id;
	private Calendar date;
	private Set<Contact> members;
	private String notes;

	@Before
	public void setUp() {
		id = 123;
		notes = "meeting notes";
		date = new GregorianCalendar();
		members = new HashSet<Contact>();
		members.add(new ContactImpl(1, "John", "Note test"));
		test = new PastMeetingImpl(id, date, members, notes);
	}


	@Test
	public void testGetNotes() {
		String actual = test.getNotes();
		assertEquals(notes, actual);
	}
	
	@Test
	public void emptyNotes() {
		test = new PastMeetingImpl(id, date, members, null);
		String actual = test.getNotes();
		assertEquals("", actual);
	}

}
