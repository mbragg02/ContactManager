package tests;

import static org.junit.Assert.*;
import impl.ContactImpl;
import impl.MeetingImpl;
import interfaces.Contact;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MeetingImplTest {

	private MeetingImpl test;
	private Set<Contact> members;
	private Calendar date;
	private int id;
	
	@Before
	public void setUp() {
		id = 12345;
		date = new GregorianCalendar();
		members = new HashSet<Contact>();
		members.add(new ContactImpl(1, "John", "Note test"));
		test = new MeetingImpl(id, date, members);
	}

	@Test
	public void testGetId() {
		int actual = test.getId();
		assertEquals(id, actual);
	}

	@Test
	public void testGetDate() {
		Calendar actual = test.getDate();
		assertEquals(date, actual);
	}

	@Test
	public void testGetContacts() {
		Set<Contact> actual = test.getContacts();
		assertEquals(members, actual);
	}

}
