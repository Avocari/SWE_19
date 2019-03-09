package at.tugraz.ist.swe.stopwatch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClockUnitTest {
	Clock clock;

	@Before
	public void setUp() throws Exception {
		clock = new Clock();
	}

	@Test
	public void testInitialState() {
		assertNotNull(clock);
		assertFalse(clock.isRunning());
		assertEquals(0L, clock.getElapsedTime());
		assertEquals("0:00:00", clock.getElapsedTimeString());
	}

	@Test
	public void testStartSetsRunning() {
		clock.start();

		assertTrue(clock.isRunning());
	}

	@Test
	public void testStartElapsesTime() throws InterruptedException {
		clock.start();
		Thread.sleep(100);

		assertNotEquals(0, clock.getElapsedTime());
		assertNotEquals("0:00:00", clock.getElapsedTimeString());
	}
}
