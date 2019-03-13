# Git Setup

```
git clone git@github.com:sw19-tug/Tutorial.git
```

Create first working Branches (develop and first feature branch)

```
git checkout -b develop
git push origin develop
git checkout -b feature/SW-001
git push origin feature/SW-001
```

# Project Setup

Now it is time to open Android Studio and create out project

* Project Name
  * Stopwatch
* Package Name
  * at.tugraz.ist.swe.stopwatch
* API
  * 19

## Adapt your gitignore

Delete all lines with /.idea* and just add /.idea

commit and push to feature branch

```
git commit -a -m "SW-001 project setup [ST,WC]"
git push origin feature/SW-001
```

Create a Pull Request

Approve changes and Merge to develop

# Button implementation

Create a new branch for your feature

```
git checkout -b feature/SW-002
```

## Tests

Copy Testfile (ExampleInstrumentedTest.java) and rename it to MainActivityEspressoTest.java

Delete @Test and function

Add check if button exists

```java
@Test
public void testButtonsVisible() {
    onView(withId(R.id.bt_reset)).check(matches(isDisplayed()));
    onView(withId(R.id.bt_start)).check(matches(isDisplayed()));
    onView(withId(R.id.bt_lap)).check(matches(isDisplayed()));
}
```

Commit and push your tests

## Implementation

Open the xml file (actvity_main.xml)

Change the ConstraintLayout to a Linear Layout and add a gravity to the header

```java
android:gravity="bottom"
```

Then add your Buttons inside another Linear Layout, give them id's and a text string

```java
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="10dp">
    <Button
        android:id="@+id/bt_reset"
        android:text="@string/reset"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/bt_start"
        android:text="@string/start"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/bt_lap"
        android:text="@string/lap"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="wrap_content" />

</LinearLayout>
```

To get the test working, you need to add a line to your build.gradle file

```java
androidTestImplementation 'com.android.support.test:rules:1.0.2'
```

Then define your rule in the MainActivityEspressoTest.java

```java
@Rule
public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
```

Test again and confirm that it works

Commit and push your implementation

Create a Pullrequest on Github and merge it into develop

# TextView implementation

Create a new branch for your feature

```
git checkout -b feature/SW-003
```

## Tests

Create a new test for your implementation

```java
@Test
public void testClockTextViewVisible() {
    onView(withId(R.id.tv_clock)).check(matches(isDisplayed()));
    onView(withId(R.id.tv_clock)).check(matches(withText("0:00:00")));
}
```

Commit and push your tests

## Implementation

Add TextView to your activity_main.xml and change the outermost LinearLayout to a RelativeLayout

```java
<TextView
    android:id="@+id/tv_clock"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="0:00:00"
    android:layout_centerInParent="true"
    android:textSize="50sp"
/>
```

And add a layout_alignParentBottom to the LinearLayout containing the Buttons

```java
android:layout_alignParentBottom="true"
```

Rerun the EspressoTests and confirm that they pass

Commit and push your implementation

Create a Pullrequest on Github and merge it into develop

# StartButton functionalty

Create a new branch for your feature

```
git checkout -b feature/SW-004
```

## Tests

Create a test to check if the TextView changes when pressing the start button

```java
@Test
public void testClockTextViewElapsesTime() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());
    Thread.sleep(100);
    onView(withId(R.id.tv_clock)).check(matches(not(withText("0:00:00"))));
}
```

commit and push your test

## Implementation

Create a new Class called Clock

```java
package at.tugraz.ist.swe.stopwatch;

public class Clock {
}
```

Add this Clock to your MainActivity, find your StartButton and create an onClickListener for it executing an empty function

```java
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
```

commit and push your implementation

## Tests

Since we created a new Class we now need to test it using JUnit Tests

```java
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
```

commit and push your tests

## Implementation

Add a Dummy Clock method for every test so they pass

```java
public boolean isRunning() {
    return false;
}

public long getElapsedTime() {
    return 0;
}

public String getElapsedTimeString() {
    return "0:00:00";
}
```

commit and push your implementation

## Tests

Add more tests to your ClockUnitTest and set up a setUp routine

```java
Clock clock;

@Before
public void setUp() throws Exception {
    clock = new Clock();
}

@Test
public void testStartSetsRunning() {
    clock.start();

    assertTrue(clock.isRunning());
}

@Test
public void testStartElapsesTime() {
    clock.start();

    assertNotEquals(0, clock.getElapsedTime());
    assertNotEquals("0:00:00", clock.getElapsedTimeString());
}
```

commit and push your tests

## Implementation

Create a interface SystemTimeProvider (SystemTimeProvider.java)

```java
package at.tugraz.ist.swe.stopwatch;

public interface SystemTimeProvider {
    long getElapsedRealTime();
}

```

And a Class for your interface called AndroidSystemTimeProvider.java

```java
package at.tugraz.ist.swe.stopwatch;

import android.os.SystemClock;

public class AndroidSystemTimeProvider implements SystemTimeProvider {
    @Override
    public long getElapsedRealTime() {
        return SystemClock.elapsedRealtime();
    }
}
```

Since our JUnit tests cannot use the Android SystemClock we need to mock a DummyClock (DummySystemTimeProvider.java) in our tests package returning us dummy values to test our clock implementation

```java
package at.tugraz.ist.swe.stopwatch;

import java.util.Arrays;
import java.util.List;

class DummySystemTimeProvider implements SystemTimeProvider {
    private List<Long> times = Arrays.asList(0L, 1000L, 2000L, 3000L, 4000L, 5000L);
    private int currentTimeIndex;

    @Override
    public long getElapsedRealTime() {
        return times.get(currentTimeIndex++ % times.size());
    }
}
```

Now add some functionality to our Clock class

First give it some member variables and a constructor

```java
private final SystemTimeProvider systemTimeProvider;
private boolean running;
private long startTime;

public Clock(SystemTimeProvider systemTimeProvider) {
    this.systemTimeProvider = systemTimeProvider;
}
```

Then adapt our Methods and add a start method

```java
public boolean isRunning() {
    return running;
}

public long getElapsedTime() {
    return running ? systemTimeProvider.getElapsedRealTime() - startTime : 0L;
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
    startTime = systemTimeProvider.getElapsedRealTime();
}
```

Finally add this functionality to our MainActivity

Add some members

```java
private Clock clock;
private Handler handler;
private TextView clockTextView;
```

edit the onCreate method

```java
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
```

And also the onStartButtonClicked method

```java
private void onStartButtonClicked() {
    clock.start();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
                onClockCallback();
            }
        }, 100);
}

    private void onClockCallback() {
        clockTextView.setText(clock.getElapsedTimeString());
    }
```

With this implementation we also need to adapt our Tests to accept new arguments in methods

in ClockUnitTest we need to add our Dummy as the systemTimeProvider

```java
SystemTimeProvider systemTimeProvider = new DummySystemTimeProvider();
clock = new Clock(systemTimeProvider);
```

and in our EspressoTest we adapt the test so it fails since our clock just increments once

```java
@Test
public void testClockTextViewElapsesTime() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());

    Thread.sleep(100);

    TextView textViewClock = mainActivityTestRule.getActivity().findViewById(R.id.tv_clock);
    String currentElapsedTime = textViewClock.getText().toString();

    Thread.sleep(100);
    onView(withId(R.id.tv_clock)).check(matches(not(withText(currentElapsedTime))));
}
```

Commit and push your implementation

## Last changes

Now we finally just tackle this problem of the clock stopping

In the MainActivity we now need to adapt the 
onClockCallback

```java
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
```

Commit and push these changes

Create a Pullrequest on Github and merge it into develop

# ======== End of Tutorial =======

# PauseButton functionalty

Create a new branch for your feature

```
git checkout -b feature/SW-005
```

## Tests

First add an EspressoTest for your PauseButton

```java
@Test
public void testClockPauseButtonStopsTime() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());

    Thread.sleep(100);

    onView(withId(R.id.bt_start)).perform(click());

    TextView textViewClock = mainActivityTestRule.getActivity().findViewById(R.id.tv_clock);
    String currentElapsedTime = textViewClock.getText().toString();

    Thread.sleep(100);

    onView(withId(R.id.tv_clock)).check(matches(withText(currentElapsedTime)));
}

@Test
public void testStartButtonRenamesButtonText() throws InterruptedException {
onView(withId(R.id.bt_start)).perform(click());
onView(withId(R.id.bt_start)).check(matches(withText(R.string.pause)));
}

```

And some tests for your Clock class

```java
@Test
public void testPauseSetsRunning() {
    clock.start();
    clock.pause();

    assertFalse(clock.isRunning());
}

@Test
public void testPauseDoesNotElapseTime() {
    clock.start();
    clock.pause();

    assertEquals(1000L, clock.getElapsedTime());
    assertEquals(1000L, clock.getElapsedTime());
}
```

## Implementation

First let's adapt our Clock class

```java
public long getElapsedTime() {
    return running ? systemTimeProvider.getElapsedRealTime() - startTime : offsetTime;
}

public void pause() {
    running = false;
    offsetTime = systemTimeProvider.getElapsedRealTime();
}
```

And also refactor our MainActivity, adding the new functionality to change the buttontext and functions

```java
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
        Button startButton = findViewById(R.id.bt_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartButtonClicked();
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

    private void onClockCallback() {
        clockTextView.setText(clock.getElapsedTimeString());

        handler.postDelayed(r, 50);
    }
}
```

## Tests

Since we still encounter one problem: the clock resets after pausing we need more tests:

First our JUnit Test

```java
@Test
public void testResumeDoesNotResetTime() {
    clock.start();
    clock.pause();
    clock.start();

    assertTrue(clock.getElapsedTime() > 1000L);
}
```

and then an EspressoTest

```java
@Test
public void testPauseButtonRenamesButtonText() {
    onView(withId(R.id.bt_start)).perform(click());
    onView(withId(R.id.bt_start)).perform(click());
    onView(withId(R.id.bt_start)).check(matches(withText(R.string.start)));
}
```

## Implementation

To fix this we just need to add our offset and start time to the time calculation when starting and pausing the clock

```java
public void start() {
    running = true;
    startTime = systemTimeProvider.getElapsedRealTime() - offsetTime;
}

public void pause() {
    running = false;
    offsetTime = systemTimeProvider.getElapsedRealTime() - startTime;
}
```

# ResetButton functionalty

Create a new branch for your feature

```
git checkout -b feature/SW-006
```

## Tests

The last step is coming up and again we need tests at the beginning

```java
@Test
public void testResetButtonResetsTextView() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());
    Thread.sleep(100);
    onView(withId(R.id.bt_reset)).perform(click());
    onView(withId(R.id.tv_clock)).check(matches(withText("0:00:00")));
}

@Test
public void testResetButtonRenamesButtonText() {
    onView(withId(R.id.bt_start)).perform(click());
    onView(withId(R.id.bt_reset)).perform(click());

    onView(withId(R.id.bt_start)).check(matches(withText(R.string.start)));
}

@Test
public void testResetButtonDoesNothingInitially() {
    onView(withId(R.id.bt_reset)).perform(click());

    onView(withId(R.id.bt_start)).check(matches(withText(R.string.start)));
    onView(withId(R.id.tv_clock)).check(matches(withText("0:00:00")));
}

@Test
public void testResetButtonDoesNotElapseTime() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());
    onView(withId(R.id.bt_reset)).perform(click());

    TextView textViewClock = mainActivityTestRule.getActivity().findViewById(R.id.tv_clock);
    String currentElapsedTime = textViewClock.getText().toString();

    Thread.sleep(100);

    onView(withId(R.id.tv_clock)).check(matches(withText(currentElapsedTime)));
}
```

## Implementation

Add an empty class to Clock.java

```java
public void reset() {
}
```

And you Button and onClickHandler to your MainActivity

```java
Button resetButton = findViewById(R.id.bt_reset);

resetButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onResetButtonClicked();
    }
});

private void onResetButtonClicked() {
    handler.removeCallbacks(r);
    clock.reset();
    startButton.setText(R.string.start);
    updateClockTextView();
}

private void updateClockTextView() {
    clockTextView.setText(clock.getElapsedTimeString());
s}
```

## Tests

Add more JUnit Tests for your implementation

```java
@Test
public void testResetButtonInitalState() {
    clock.reset();

    assertFalse(clock.isRunning());
    assertEquals(0L, clock.getElapsedTime());
}

@Test
public void testResetButtonSetsRunning() {
    clock.start();
    clock.reset();

    assertFalse(clock.isRunning());
}

@Test
public void testStartAfterReset() {
    clock.start();

    assertEquals(1000L, clock.getElapsedTime());

    clock.reset();

    assertEquals(0L, clock.getElapsedTime());

    clock.start();

    assertEquals(1000L, clock.getElapsedTime());
}

@Test
public void testResetButtonResetsTime() {
    clock.start();
    clock.reset();

    assertEquals(0L, clock.getElapsedTime());
}
```

## Implementation

Add a dummy implementation

```java
public void reset() {
    running = false;
}
```

## Tests

Add another MainActivityTest

```java
@Test
public void testResetButtonResetsAfterPause() throws InterruptedException {
    onView(withId(R.id.bt_start)).perform(click());
    Thread.sleep(100);
    onView(withId(R.id.bt_start)).perform(click());
    onView(withId(R.id.bt_reset)).perform(click());

    onView(withId(R.id.tv_clock)).check(matches(withText("0:00:00")));
}
```

## Implementation

Fix your reset implemenation

```java
public void reset() {
    running = false;
    offsetTime = 0;
}
```

Commit and push all of your tests and implementation and you are done!

# Bonus

## Give UI more style

in app/main/res/drawable add a new xml file with the following content

```java
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="ring" android:thickness="5dp" android:useLevel="false">
    <solid android:color="@color/colorPrimary" />
</shape>
```

Add this circle around your TextView

```java
<ImageView
    android:layout_width="300dp"
    android:layout_height="300dp"
    android:src="@drawable/circle"
    android:layout_centerInParent="true"
    tools:ignore="ContentDescription" />
```

You can also play around with the style of buttons and add a backgroundTint.