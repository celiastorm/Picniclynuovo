package com.picnicly.picnic_ly;

/**
 * Created by Giovy on 30/01/2017.
 */

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        // Don't do this! This is just so cold launches take some time
        //SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }
}
