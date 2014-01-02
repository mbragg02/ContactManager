package tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import impl.ContactManagerImpl;
import interfaces.Contact;
import interfaces.ContactManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ContactManagerImplTest {


	private static ContactManager test;
	private Set<Contact> contacts;
	private Calendar futureDate;
	private Calendar pastDate;
	private int contactId;
	private int meetingId;


	@Before
	public void setUp() {
		

		test = new ContactManagerImpl();
		contactId = 0;
		meetingId = 0;
		// Create 3 test contacts
		test.addNewContact("adam", "adams notes");
		test.addNewContact("bob", "bobs notes");
		test.addNewContact("chris", "chris's notes");
		// Create a future and past date
		futureDate = Calendar.getInstance();
		futureDate.set(2015, 1, 1);
		pastDate = Calendar.getInstance();
		pastDate.set(2000, 1, 1);
		// create a contact set with the 1st contact in
		contacts = test.getContacts(0);

	}


	@Ignore
	public void testFlush() {
		fail("Not yet implemented"); // TODO
	}



	// Contacts setters

	@Test
	public void testAddNewContact() {
		int actual = test.getContacts("adam").size();
		assertEquals(1, actual);
	}

	// Test a Null contact name argument
	@Test(expected = NullPointerException.class)
	public void testContactNameNull() {
		test.addNewContact(null, "adams notes");
	}

	// Test a Null notes argument
	@Test(expected = NullPointerException.class)
	public void testContactNotesNull() {
		test.addNewContact("adam", null);
	}


	// Contacts getters
	@Test
	public void testGetContactsIntArray() {	
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(test.getContacts(0).iterator().next());
		assertEquals(expected, test.getContacts(0));
	}
	// Test IDs passed that are not recognized by the contacts manager
	@Test(expected = IllegalArgumentException.class)
	public void getContactsIntArrayIllegalArg() {
		test.getContacts(999);
	}

	@Test
	public void testGetContactsString() {
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(test.getContacts(0).iterator().next());
		assertEquals(expected, test.getContacts("adam"));
	}

	// Tests a contact name that is not recognized by the contacts manager
	@Test
	public void nonMatchingGetContact() {
		Set<Contact> output = test.getContacts("xxxxx");
		Set<Contact> expected = new TreeSet<Contact>();
		assertEquals(output, expected);
	}

	// Tests for a null argument being passed to the getContacts method.
	@Test(expected = NullPointerException.class)
	public void getContactNull() {
		test.getContacts();
	}







	// Future Meetings setters

	@Test
	public void testAddFutureMeeting() {
		// addFutureMeeting returns the meeting id for the meeting it creates.
		assertEquals(0, test.addFutureMeeting(contacts, futureDate));
	}
	@Test(expected = IllegalArgumentException.class)
	public void addFutureMeetingInPast() {
		test.addFutureMeeting(contacts, pastDate);
	}
	@Test(expected = NullPointerException.class)
	public void addFutureMeetingNullDate() {
		assertEquals(0, test.addFutureMeeting(contacts, null));
	}        

	@Test(expected = NullPointerException.class)
	public void addFutureMeetingNull() {
		assertEquals(0, test.addFutureMeeting(null, futureDate));
	} 




	// Past Meeting setters

	@Test
	public void testAddNewPastMeeting() {
		test.addNewPastMeeting(contacts, pastDate, "past meeting note");
		assertEquals("past meeting note", test.getPastMeeting(0).getNotes());
	}
	@Test(expected = IllegalArgumentException.class)
	public void addNewPastMeetingFutureDate() {
		test.addNewPastMeeting(contacts, futureDate, "past meeting note");
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullNotes() {
		test.addNewPastMeeting(contacts, pastDate, null);
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullDate() {
		test.addNewPastMeeting(contacts, null, "past meeting note");
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullContacts() {
		test.addNewPastMeeting(null, pastDate, "past meeting note");
	}




	// Future Meeting getters

	@Test
	public void testGetFutureMeeting() {
		test.addFutureMeeting(contacts, futureDate);
		assertEquals(contacts, test.getFutureMeeting(0).getContacts());
	}

	// Tests that an IllegalArgumentException is thrown if a meeting is found in the past.
	@Test(expected = IllegalArgumentException.class)
	public void getFutureMeetingPastMeetingID() {
		test.addNewPastMeeting(contacts, pastDate, "test");
		test.getFutureMeeting(0);
	}
	@Test
	public void getFutureMeetingNull() {
		assertNull(test.getFutureMeeting(999));
	}



	@Test
	public void testGetFutureMeetingListCalendar() {
		test.addFutureMeeting(contacts, futureDate);
		assertEquals(contacts, test.getFutureMeetingList(futureDate).iterator().next().getContacts());
	}

	@Test
	public void testGetFutureMeetingListContact() {
		fail("Not yet implemented"); // TODO
	}



	// Past Meeting Getters

	@Test
	public void testGetPastMeeting() {
		test.addNewPastMeeting(contacts, pastDate, "past meeting note");
		System.out.println(test.getPastMeeting(0).getId());
		assertEquals(0, test.getPastMeeting(0).getId());
	}

	// Test getting a past meeting where a future meeting is found with the supplied ID
	@Test (expected = IllegalArgumentException.class)
	public void getPastMeetingFutureMeetingID() {
		test.addFutureMeeting(contacts, futureDate);
		test.getPastMeeting(0);
	}

	@Test
	public void getPasteMeetingNull() {
		assertNull(test.getPastMeeting(999));
	}

	@Test
	public void testGetPastMeetingList() {
		fail("Not yet implemented"); // TODO
	}








	@Test
	public void testAddMeetingNotes() {
		fail("Not yet implemented"); // TODO
	}


	@Test
	public void testGetMeeting() {
		fail("Not yet implemented"); // TODO
	}

}
