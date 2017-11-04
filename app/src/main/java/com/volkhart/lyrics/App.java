package com.volkhart.lyrics;

import android.app.Application;
import android.os.StrictMode;

import com.volkhart.feedback.FeedbackTree;

import timber.log.Timber;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(FeedbackTree.INSTANCE);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .penaltyDeath()
                    .build());
        }
    }
}
