package com.ricardorainha.interactiveexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class InteractiveRegistryJobService extends JobService {

    private AsyncTask<Void, Void, Void> task = null;


    @Override
    public boolean onStartJob(final JobParameters job) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Intent serviceIntent = new Intent(getApplicationContext(), InteractiveRegistryService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }
                else {
                    startService(serviceIntent);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };
        task.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (task != null) {
            task.cancel(true);
        }
        return true;
    }
}
