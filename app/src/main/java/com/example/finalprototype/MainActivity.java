package com.example.finalprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.DEFAULT_VIBRATE;

public class MainActivity extends AppCompatActivity {
    private static final String MY_PREF = "MY_APP";
    private static final String EXERCISES_KEY = "exercises";
    private static final String WORK_TIME_KEY = "workTime";
    private static final String BREAK_TIME_KEY = "breakTime";
    private static final String YOUTUBE_ID = "youTubeId";
    private static final long COUNTDOWN_INTERVAL = 1000;
    private static final String CHANNEL_ID = "channel2";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private TextView workTimeText;
    private TextView breakTimeText;
    private CountDownTimer countDownTimer;
    private Button startWorkTimerBtn;
    private NotificationManagerCompat notificationManager;
    private long timeLeftMillisWorkTime;
    private long timeLeftMillisBreakTime;
    private long millisInFutureWorkTime;
    private long millisInFutureBreakTime;
    private long workEndTime;
    private long breakEndTime;
    private boolean timerRunning;
    private boolean workTimerRunning;
    private boolean breakTimerRunning;
    private Random rand;
    ArrayList<Data> exercises = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExercises();

        mPrefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        saveData(EXERCISES_KEY, exercises);

        notificationManager = NotificationManagerCompat.from(this);

        workTimeText = findViewById(R.id.workTime);
        breakTimeText = findViewById(R.id.breakTime);

        displayTime();

        startWorkTimerBtn = findViewById(R.id.startWorkTimer);
        startWorkTimerBtn.setOnClickListener(v -> {
            startWorkTimeCountDownTimer();
        });

        displayBottomNavigation();
    }

    private void createExercises() {
        exercises = new ArrayList<>();
        exercises.add(new Data("Beginner Stretch", "L_xrDAtykMI"));
        exercises.add(new Data("Full body Stretch", "g_tea8ZNk5A"));
        exercises.add(new Data("Neck Stretch", "u3Ocw5UIpYs"));
        exercises.add(new Data("Back Stretch", "bEDH_uTcdf4"));
        exercises.add(new Data("Shoulder Stretch", "6jHsraw2NIk"));
    }

    private void saveData(String name, ArrayList<Data> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor = mPrefs.edit();
        editor.putString(name, json);
        editor.apply();
    }

    private int getData(String name) {
        return mPrefs.getInt(name, 0);
    }

    private void displayTime() {
        millisInFutureWorkTime = getData(WORK_TIME_KEY);
        millisInFutureBreakTime = getData(BREAK_TIME_KEY);

        if (millisInFutureWorkTime != 0) {
            timeLeftMillisWorkTime = millisInFutureWorkTime;
            setWorkTimeText();
        }

        if (millisInFutureBreakTime != 0) {
            timeLeftMillisBreakTime = millisInFutureBreakTime;
            setBreakTimeText();
        }
    }

    private void startWorkTimeCountDownTimer() {
        System.out.println("Work time timer is called ########################");
        workEndTime = System.currentTimeMillis() + timeLeftMillisWorkTime;
        startWorkTimerBtn.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(timeLeftMillisWorkTime, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillisWorkTime = millisUntilFinished;
                setWorkTimeText();
            }

            @Override
            public void onFinish() {
                timeLeftMillisWorkTime = millisInFutureWorkTime;
                workTimerRunning = false;
                setWorkTimeText();
                rand = new Random();
                notification();
                startBreakTimeCountDownTimer();
            }
        }.start();

        workTimerRunning = true;
    }

    private void setWorkTimeText() {

        long seconds = (timeLeftMillisWorkTime / 1000) % 60;
        long minutes = (timeLeftMillisWorkTime / (1000 * 60)) % 60;
        long hours = timeLeftMillisWorkTime / (1000 * 60 * 60);

        String text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        workTimeText.setText(text);
    }

    private void startBreakTimeCountDownTimer() {
        startWorkTimerBtn.setVisibility(View.GONE);
        breakEndTime = System.currentTimeMillis() + timeLeftMillisBreakTime;
        countDownTimer = new CountDownTimer(timeLeftMillisBreakTime, COUNTDOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillisBreakTime = millisUntilFinished;
                setBreakTimeText();
            }

            @Override
            public void onFinish() {
                timeLeftMillisBreakTime = millisInFutureBreakTime;
                breakTimerRunning = false;
                setBreakTimeText();
                startWorkTimeCountDownTimer();
            }
        }.start();
        breakTimerRunning = true;
    }

    private void setBreakTimeText() {
        long seconds = (timeLeftMillisBreakTime / 1000) % 60;
        long minutes = (timeLeftMillisBreakTime / (1000 * 60)) % 60;

        String text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        breakTimeText.setText(text);
    }

    private void displayBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    Intent settingsIntent = new Intent(this, Settings.class);
                    startActivity(settingsIntent);
                    break;
                case R.id.setTimes:
                    Intent setTimeIntent = new Intent(this, SetTime.class);
                    startActivity(setTimeIntent);
                    break;
                case R.id.exercises:
                    Intent exerciseIntent = new Intent(this, Exercises.class);
                    startActivity(exerciseIntent);
                    break;
            }
            return true;
        });
    }

    private void notification() {
        Intent intent = new Intent(this, Video.class);

        int index = rand.nextInt(exercises.size());
        String youTubeId = exercises.get(index).getUrl();
        intent.putExtra(YOUTUBE_ID, youTubeId);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String title = "Break Notification";
        String message = "It's time to take a break and do some stretch exercise!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(DEFAULT_VIBRATE)
                .setAutoCancel(true);

        createNotificationChannel();
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel 1";
            String description = "This is channel 1";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startWorkTimerBtn.setVisibility(View.GONE);

        if (workTimerRunning) {
            System.out.println("");
            editor.putLong("millisLeftWorkTime", timeLeftMillisWorkTime);
            editor.putBoolean("workTimerRunning", workTimerRunning);
            editor.putLong("workEndTime", workEndTime);
            editor.apply();
        }

        if (breakTimerRunning) {
            editor.putLong("millisLeftBreakTime", timeLeftMillisBreakTime);
            editor.putBoolean("breakTimerRunning", breakTimerRunning);
            editor.putLong("breakEndTime", breakEndTime);
            editor.apply();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        timeLeftMillisWorkTime = mPrefs.getLong("millisLeftWorkTime", millisInFutureWorkTime);
        workTimerRunning = mPrefs.getBoolean("workTimerRunning", false);

        timeLeftMillisBreakTime = mPrefs.getLong("millisLeftBreakTime", millisInFutureBreakTime);
        breakTimerRunning = mPrefs.getBoolean("breakTimerRunning", false);

        setWorkTimeText();

        setBreakTimeText();

        if (workTimerRunning) {
            workEndTime = mPrefs.getLong("workEndTime", 0);
            timeLeftMillisWorkTime = workEndTime - System.currentTimeMillis();

            if (timeLeftMillisWorkTime < 0) {
                timeLeftMillisWorkTime = millisInFutureWorkTime;
                workTimerRunning = false;
                setWorkTimeText();
            } else {
                startWorkTimeCountDownTimer();
            }
        }

        if (breakTimerRunning) {
            breakEndTime = mPrefs.getLong("breakEndTime", 0);
            timeLeftMillisBreakTime = breakEndTime - System.currentTimeMillis();

            if (timeLeftMillisBreakTime < 0) {
                timeLeftMillisBreakTime = millisInFutureBreakTime;
                breakTimerRunning = false;
                setBreakTimeText();
            } else {
                startBreakTimeCountDownTimer();
            }
        }
    }

}