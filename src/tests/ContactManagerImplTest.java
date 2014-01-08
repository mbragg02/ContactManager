package tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import impl.*;
import interfaces.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

// Note
public class ContactManagerImplTest {

	private static ContactManager contactManagerTest;
	private Set<Contact> contacts;
	private Calendar futureDate;
	private Calendar pastDate;

	@Before
	public void setUp() {
		contactManagerTest = new ContactManagerImpl();

		// Create 3 test contacts
		contactManagerTest.addNewContact("adam", "adams notes");
		contactManagerTest.addNewContact("bob", "bobs notes");
		contactManagerTest.addNewContact("chris", "chris's notes");
		
		// Create a future and past date
		futureDate = Calendar.getInstance();
		futureDate.set(2015, 1, 1);
		pastDate = Calendar.getInstance();
		pastDate.set(2000, 1, 1);
		
		// create a contact set with the 1st contact in
		contacts = contactManagerTest.getContacts(0);
	}


	@Ignore
	public void testFlush() {
	}

	// Contacts setters

	@Test
	public void testAddNewContact() {
		contactManagerTest.addNewContact("terry", "terrys notes");
		String actual = contactManagerTest.getContacts("terry").iterator().next().getName();
		assertEquals("terry", actual);
	}

	// Test a Null contact name argument
	@Test(expected = NullPointerException.class)
	public void testContactNameNull() {
		contactManagerTest.addNewContact(null, "adams notes");
	}

	// Test a Null notes argument
	@Test(expected = NullPointerException.class)
	public void testContactNotesNull() {
		contactManagerTest.addNewContact("terry", null);
	}


	// Contacts getters
	@Test
	public void testGetContactsIntArray() {	
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(contactManagerTest.getContacts(0).iterator().next());
		assertEquals(expected, contactManagerTest.getContacts(0));
	}
	// Test IDs passed that are not recognized by the contacts manager
	@Test(expected = IllegalArgumentException.class)
	public void getContactsIntArrayIllegalArg() {
		contactManagerTest.getContacts(999);
	}

	@Test
	public void testGetContactsString() {
//		Set<Contact> expected = contactManagerTest.getContacts(0);
		Set<Contact> actual = contactManagerTest.getContacts("adam");
//		assertTrue(contacts.equals(actual));
		assertEquals(contacts, actual);
	}

	// Tests a contact name that is not recognised by the contacts manager returns an empty set
	@Test
	public void nonMatchingGetContact() {
		Set<Contact> output = contactManagerTest.getContacts("xxxxx");
		Set<Contact> expected = new TreeSet<Contact>();
		assertEquals(output, expected);
	}

	// Tests for a null argument being passed to the getContacts method.
	@Test(expected = NullPointerException.class)
	public void getContactNull() {
		contactManagerTest.getContacts();
	}







	// Future Meetings setters

	@Test
	public void testAddFutureMeeting() {
		// addFutureMeeting returns the meeting id for the meeting it creates.
		assertEquals(0, contactManagerTest.addFutureMeeting(contacts, futureDate));
	}
	@Test(expected = IllegalArgumentException.class)
	public void addFutureMeetingInPast() {
		contactManagerTest.addFutureMeeting(contacts, pastDate);
	}
	@Test(expected = NullPointerException.class)
	public void addFutureMeetingNullDate() {
		assertEquals(0, contactManagerTest.addFutureMeeting(contacts, null));
	}        

	@Test(expected = NullPointerException.class)
	public void addFutureMeetingNull() {
		assertEquals(0, contactManagerTest.addFutureMeeting(null, futureDate));
	} 




	// Past Meeting setters

	@Test
	public void testAddNewPastMeeting() {
		contactManagerTest.addNewPastMeeting(contacts, pastDate, "past meeting note");
		assertEquals("past meeting note", contactManagerTest.getPastMeeting(0).getNotes());
	}
	@Test(expected = IllegalArgumentException.class)
	public void addNewPastMeetingFutureDate() {
		contactManagerTest.addNewPastMeeting(contacts, futureDate, "past meeting note");
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullNotes() {
		contactManagerTest.addNewPastMeeting(contacts, pastDate, null);
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullDate() {
		contactManagerTest.addNewPastMeeting(contacts, null, "past meeting note");
	}
	@Test(expected = NullPointerException.class)
	public void addNewPastMeetingNullContacts() {
		contactManagerTest.addNewPastMeeting(null, pastDate, "past meeting note");
	}




	// Future Meeting getters

	@Test
	public void testGetFutureMeeting() {
		contactManagerTest.addFutureMeeting(contacts, futureDate);
		assertEquals(contacts, contactManagerTest.getFutureMeeting(0).getContacts());
	}

	// Tests that an IllegalArgumentException is thrown if a meeting is found in the past.
	@Test(expected = IllegalArgumentException.class)
	public void getFutureMeetingPastMeetingID() {
		contactManagerTest.addNewPastMeeting(contacts, pastDate, "test");
		contactManagerTest.getFutureMeeting(0);
	}
	
	@Test
	public void getFutureMeetingNull() {
		assertNull(contactManagerTest.getFutureMeeting(999));
	}

	@Test
	public void testGetFutureMeetingListCalendar() {
		contactManagerTest.addFutureMeeting(contacts, futureDate);
		assertEquals(contacts, contactManagerTest.getFutureMeetingList(futureDate).iterator().next().getContacts());
	}

	@Test
	public void testGetFutureMeetingListContact() {
		// Create a new future meeting and add it to a new past meeting list
		FutureMeeting newFutureMeeting = new FutureMeetingImpl(0, futureDate, contacts);
		List<FutureMeeting> expectedFutureMeetingList = new LinkedList<FutureMeeting>();
		expectedFutureMeetingList.add(newFutureMeeting);
		
		// Add the same future meeting to contactManagerTest
		contactManagerTest.addFutureMeeting(newFutureMeeting.getContacts(), newFutureMeeting.getDate());
			
		Set<Contact> contact =  contactManagerTest.getContacts(0);
		// get the futureMeetingList for the same contact (the First contact added in the @before initialise)
		List<Meeting> futureMeetingList = contactManagerTest.getFutureMeetingList(contact.iterator().next());
		
		// Test if the two lists contain the same contact.
		assertEquals(expectedFutureMeetingList.contains(contact), futureMeetingList.contains(contact));	
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getFutureMeetingListNullContact() {
		Contact contact = new ContactImpl(99, "peter", "peters notes");
		contactManagerTest.getFutureMeetingList(contact);
	}


	
	// Past Meeting Getters
	@Test
	public void testGetPastMeeting() {
		contactManagerTest.addNewPastMeeting(contacts, pastDate, "past meeting note");
		assertEquals(0, contactManagerTest.getPastMeeting(0).getId());
	}

	// Test getting a past meeting where a future meeting is found with the supplied ID
	@Test (expected = IllegalArgumentException.class)
	public void getPastMeetingFutureMeetingID() {
		contactManagerTest.addFutureMeeting(contacts, futureDate);
		contactManagerTest.getPastMeeting(0);
	}

	@Test
	public void getPasteMeetingNull() {
		assertNull(contactManagerTest.getPastMeeting(999));
	}

	@Test
	public void testGetPastMeetingList() {
		// Create a new past meeting and add it to a new past meeting list
		PastMeeting newPastMeeting = new PastMeetingImpl(0, pastDate, contacts, "Past meeting note");
		List<PastMeeting> expectedPastMeetingList = new LinkedList<PastMeeting>();
		expectedPastMeetingList.add(newPastMeeting);
		
		// Add the same past meeting to contactManagerTest
		contactManagerTest.addNewPastMeeting(newPastMeeting.getContacts(), newPastMeeting.getDate(), newPastMeeting.getNotes());
			
		Set<Contact> contact =  contactManagerTest.getContacts(0);
		// get the pastMeetingList for the same contact (the First contact added in the @before initialise)
		List<PastMeeting> pastMeetingList = contactManagerTest.getPastMeetingList(contact.iterator().next());
		
		// Test if the two lists contain the same contact.
		assertEquals(expectedPastMeetingList.contains(contact), pastMeetingList.contains(contact));	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getPastMeetingListNullContact() {
		Contact contact = new ContactImpl(99, "peter", "peters notes");
		contactManagerTest.getPastMeetingList(contact);
	}





	// Tests for adding notes

	@Test
	public void testAddMeetingNotes() {
		contactManagerTest.addNewPastMeeting(contacts, pastDate, "first");
		contactManagerTest.addMeetingNotes(0, "second");
		assertTrue(contactManagerTest.getPastMeeting(0).getNotes().equals("first second"));
	}
	
	// If the notes passed are null
	@Test(expected = NullPointerException.class)
	public void addNullMeetingNotes(){
		contactManagerTest.addMeetingNotes(0, null);
	}
	
	// If the meeting does not exists
	@Test(expected = IllegalArgumentException.class)
	public void addMeetingNotesNonexistent (){
		contactManagerTest.addMeetingNotes(0, "notes");
	}
	
	// Meeting set for a date in the future
	@Test(expected = IllegalStateException.class)
	public void addFutureMeetingNotes(){
		contactManagerTest.addFutureMeeting(contacts, futureDate);
		contactManagerTest.addMeetingNotes(0, "notes");
	}


}
