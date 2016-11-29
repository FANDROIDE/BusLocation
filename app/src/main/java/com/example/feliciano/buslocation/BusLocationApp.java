package com.example.feliciano.buslocation;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by feliciano on 29/11/16.
 */

public class BusLocationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
