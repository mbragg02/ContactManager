package tests;

import static org.junit.Assert.*;
import impl.ContactImpl;
import interfaces.Contact;

import org.junit.Before;
import org.junit.Test;

public class ContactImplTest {
	private Contact test;
	private String note = "Notes test";
	private String name = "John";
	
	@Before
	public void setUp() {
		test = new ContactImpl(12345, name, note);
	}


	@Test
	public void testGetId() {
		int output = test.getId();
		int expected = 12345;
		assertEquals(output, expected);
	}

	@Test
	public void testGetName() {
		String output = test.getName();
		String expected = name;
		assertEquals(output, expected);
	}

	@Test
	public void testGetNotes() {
		String output = test.getNotes();
		String expected = note;
		assertEquals(output, expected);
	}

	@Test
	public void testAddNotes() {
		test.addNotes(note);
	}

	@Test
	public void nullName() {
		test = new ContactImpl(1, null, null);
		String name = test.getName();
		assertNull(name);
	}
	
	@Test
	public void nullNotes() {
		// If notes are null, then an empty string should be returned.
		test = new ContactImpl(1, null, null);
		String notes = test.getNotes();
		assertEquals("", notes);
	}
	
	@Test
	public void emptyNotes() {
		// If there are no notes, then an empty string should be returned.
		test = new ContactImpl(1, null, "");
		String notes = test.getNotes();
		assertEquals("", notes);
	}
}