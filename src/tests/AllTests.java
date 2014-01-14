package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ContactImplTest.class, ContactManagerImplTest.class,
		MeetingImplTest.class, PastMeetingImplTest.class })
public class AllTests {

}
