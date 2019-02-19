package com.ricardorainha.interactiveexample;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedPref {

    private static final String PREFERENCE_NAME = "InteractiveRegistry";
    private static final String PREFERENCE_REGISTRIES = "registries";
    private static final String PREFERENCE_IDLE_MODE = "idle_mode";
    private static final String PREFERENCE_IS_INTERACTIVE = "is_interactive";
    private static final String PREFERENCE_TIMESTAMP = "timestamp";


    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static int getRegistriesPref(Context context) {
        return getPreference(context).getInt(PREFERENCE_REGISTRIES, 0);
    }

    private static void increaseRegistryNumber(Context context) {
        int registriesNumber = getRegistriesPref(context);
        registriesNumber++;
        getPreference(context).edit().putInt(PREFERENCE_REGISTRIES, registriesNumber).commit();
    }

    public static String getIdleMode(Context context, int index) {
        return getPreference(context).getString(PREFERENCE_IDLE_MODE + index, "N/A");
    }

    public static String getIsInteractive(Context context, int index) {
        return getPreference(context).getString(PREFERENCE_IS_INTERACTIVE + index, "N/A");
    }

    public static String getTimestamp(Context context, int index) {
        return getPreference(context).getString(PREFERENCE_TIMESTAMP + index, "N/A");
    }

    public static void addInteractiveRegistry(Context context, boolean idleMode, boolean isInteractive) {
        int index = getRegistriesPref(context);
        getPreference(context).edit().putString(PREFERENCE_IDLE_MODE + index, String.valueOf(idleMode)).commit();
        getPreference(context).edit().putString(PREFERENCE_IS_INTERACTIVE + index, String.valueOf(isInteractive)).commit();

        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        getPreference(context).edit().putString(PREFERENCE_TIMESTAMP + index, timeStamp).commit();

        increaseRegistryNumber(context);
    }
}
