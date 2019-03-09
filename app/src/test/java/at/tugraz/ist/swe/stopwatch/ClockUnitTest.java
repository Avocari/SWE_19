package at.tugraz.ist.swe.stopwatch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClockUnitTest {
	private Clock clock;

	@Before
	public void setUp() {
		SystemTimeProvider systemTimeProvider = new DummySystemTimeProvider();
		clock = new Clock(systemTimeProvider);
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
	public void testPauseSetsRunning() {
		clock.start();
		clock.pause();

		assertFalse(clock.isRunning());
	}

	@Test
	public void testStartElapsesTime() {
		clock.start();

		assertEquals(1000L, clock.getElapsedTime());
		assertEquals("0:02:00", clock.getElapsedTimeString());
	}

	@Test
	public void testPauseDoesNotElapseTime() {
		clock.start();
		clock.pause();

		assertEquals(1000L, clock.getElapsedTime());
		assertEquals(1000L, clock.getElapsedTime());
	}

}
