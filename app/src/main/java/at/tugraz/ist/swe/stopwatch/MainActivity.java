package at.tugraz.ist.swe.stopwatch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	private Clock clock;
	private Handler handler;
	private TextView clockTextView;
	private Button startButton;
	private Runnable r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SystemTimeProvider systemTimeProvider = new AndroidSystemTimeProvider();
		clock = new Clock(systemTimeProvider);
		handler = new Handler(getMainLooper());

		clockTextView = findViewById(R.id.tv_clock);
		startButton = findViewById(R.id.bt_start);
		Button resetButton = findViewById(R.id.bt_reset);
		Button startButton = findViewById(R.id.bt_start);

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onStartButtonClicked();
			}
		});
		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onResetButtonClicked();
			}
		});


		r = new Runnable() {
			@Override
			public void run() {
				onClockCallback();
			}
		};
	}

	private void onStartButtonClicked() {
		handler.removeCallbacks(r);
		if (clock.isRunning()) {
			clock.pause();
			startButton.setText(R.string.start);
		} else {
			clock.start();
			startButton.setText(R.string.pause);
			onClockCallback();
		}
	}

	private void onResetButtonClicked() {
		handler.removeCallbacks(r);
		clock.reset();
		startButton.setText(R.string.start);
		updateClockTextView();
	}

	private void updateClockTextView() {
		clockTextView.setText(clock.getElapsedTimeString());
	}

	private void onClockCallback() {
		updateClockTextView();

		handler.postDelayed(r, 50);
	}

}
