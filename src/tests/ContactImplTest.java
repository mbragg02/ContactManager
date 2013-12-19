package tests;

import static org.junit.Assert.*;
import impl.ContactImpl;

import org.junit.Before;
import org.junit.Test;

public class ContactImplTest {
	public ContactImpl test;
	private String note = "test";
	@Before
	public void init() {
		test = new ContactImpl(12345, "michael", "test");
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
         String expected = "michael";
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
		test = new ContactImpl(1, null, "note");
		String name = test.getName();
		assertEquals(name, null);
	}

}
