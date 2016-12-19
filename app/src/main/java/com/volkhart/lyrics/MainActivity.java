package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (BuildConfig.DEBUG) {
                Timber.plant(new Timber.DebugTree());
            }
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, SongIndexFragment.newInstance(), SongIndexFragment.TAG)
                    .commit();
        }
    }

    public void showSong(Song song) {
        startActivity(SongDetailsActivity.newIntent(this, song));
    }
}
