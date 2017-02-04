package com.volkhart.lyrics;

import android.app.Application;

import com.volkhart.feedback.utils.FeedbackTree;

import timber.log.Timber;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(FeedbackTree.INSTANCE);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
