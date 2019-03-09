package at.tugraz.ist.swe.stopwatch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ClockUnitTest {
	Clock clock = new Clock();

	@Test
	public void testInitialState() {
		assertNotNull(clock);
		assertFalse(clock.isRunning());
		assertEquals(0L, clock.getElapsedTime());
		assertEquals("0:00:00", clock.getElapsedTimeString());
	}
}
