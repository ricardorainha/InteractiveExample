package com.ricardorainha.interactiveexample;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

public class InteractiveRegistryService extends IntentService {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground(12345, new Notification.Builder(this, "channel_id").build());
    }

    public InteractiveRegistryService() {
        super("InteractiveRegistryService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            boolean idleMode = powerManager.isDeviceIdleMode();
            boolean isInteractive = powerManager.isInteractive();
            SharedPref.addInteractiveRegistry(this, idleMode, isInteractive);
        }
    }
}
