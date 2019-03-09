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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SystemTimeProvider systemTimeProvider = new AndroidSystemTimeProvider();
		clock = new Clock(systemTimeProvider);
		handler = new Handler(getMainLooper());

		clockTextView = findViewById(R.id.tv_clock);
		Button startButton = findViewById(R.id.bt_start);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onStartButtonClicked();
			}
		});

	}

	private void onStartButtonClicked() {
		clock.start();
		onClockCallback();
	}

	private void onClockCallback() {
		clockTextView.setText(clock.getElapsedTimeString());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onClockCallback();
			}
		}, 100);
	}
}
