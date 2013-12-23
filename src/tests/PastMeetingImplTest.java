package tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import impl.ContactImpl;
import impl.PastMeetingImpl;
import interfaces.Contact;
import interfaces.PastMeeting;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PastMeetingImplTest {

	private PastMeeting test;
	private int id;
	private Calendar date;
	private Set<Contact> members;
	private String notes;

	@Before
	public void init() {
		id = 123;
		notes = "meeting notes";
		date = new GregorianCalendar();
		members = new HashSet<Contact>();
		members.add(new ContactImpl(1, "John", "Note test"));
		test = new PastMeetingImpl(id, date, members, notes);
	}
	@Ignore
	public void testPastMeetingImpl() {
		fail("Not yet implemented");
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
