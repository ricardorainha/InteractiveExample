package com.ricardorainha.interactiveexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    GooglePlayDriver driver;
    FirebaseJobDispatcher jobDispatcher;
    private static final int SCHEDULER_INTERVAL_SECONDS = 30 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        scheduleJobService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        textView = findViewById(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod());

        int registriesNumber = SharedPref.getRegistriesPref(this);

        String registries = "";
        for (int i = 0; i < registriesNumber; i++) {
            registries += "[" + i + "][" + SharedPref.getTimestamp(this, i) + "] " +
                    "isDeviceIdleMode: " + SharedPref.getIdleMode(this, i) +
                    " / isInteractive: " + SharedPref.getIsInteractive(this, i) + "\n";
        }
        textView.setText(registries);
    }

    private void scheduleJobService() {
        driver = new GooglePlayDriver(this);
        jobDispatcher = new FirebaseJobDispatcher(driver);

        Job interactiveJob = jobDispatcher.newJobBuilder()
                .setService(InteractiveRegistryJobService.class)
                .setTag("interactive-job")
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULER_INTERVAL_SECONDS, SCHEDULER_INTERVAL_SECONDS + 60))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        jobDispatcher.schedule(interactiveJob);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Registry Tunnel");

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
