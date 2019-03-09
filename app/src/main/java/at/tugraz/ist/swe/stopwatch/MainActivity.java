package at.tugraz.ist.swe.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
	Clock clock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		clock = new Clock();

		Button startButton = findViewById(R.id.bt_start);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onStartButtonClicked();
			}
		});
	}

	private void onStartButtonClicked() {

	}
}
