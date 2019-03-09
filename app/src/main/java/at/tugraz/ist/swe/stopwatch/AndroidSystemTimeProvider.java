package at.tugraz.ist.swe.stopwatch;

import android.os.SystemClock;

public class AndroidSystemTimeProvider implements SystemTimeProvider {
	@Override
	public long getElapsedRealTime() {
		return SystemClock.elapsedRealtime();
	}
}
