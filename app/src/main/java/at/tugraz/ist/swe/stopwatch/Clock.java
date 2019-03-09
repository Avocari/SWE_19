package at.tugraz.ist.swe.stopwatch;

import android.os.SystemClock;

import java.text.Format;

public class Clock {
	private final SystemTimeProvider systemTimeProvider;
	private boolean running;
	private long startTime;
    private long offsetTime;

    public Clock(SystemTimeProvider systemTimeProvider) {
		this.systemTimeProvider = systemTimeProvider;
	}

	public boolean isRunning() {
		return running;
    }

    public long getElapsedTime() {
        return running ? systemTimeProvider.getElapsedRealTime() - startTime : offsetTime;
    }

    public String getElapsedTimeString() {
		long elapsedTime = getElapsedTime();
		int milliseconds = (int) (elapsedTime % 1000 / 10);
		int seconds = (int) (elapsedTime / 1000 % 60);
		int minutes = (int) (elapsedTime / 1000 / 60);
        return String.format("%01d:%02d:%02d", minutes, seconds, milliseconds);
    }

	public void start() {
		running = true;
		startTime = systemTimeProvider.getElapsedRealTime() - offsetTime;
	}

	public void pause() {
		running = false;
		offsetTime = systemTimeProvider.getElapsedRealTime() - startTime;
	}

	public void reset() {
    	running = false;
	}
}
