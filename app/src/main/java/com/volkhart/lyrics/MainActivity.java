package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(SongIndexFragment.TAG) != null) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        fragmentManager.beginTransaction()
                .add(android.R.id.content, SongIndexFragment.newInstance(), SongIndexFragment.TAG)
                .commit();
    }

    public void showSong(Song song) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, SongFragment.newInstance(song), SongFragment.TAG)
                .addToBackStack(null)
                .commit();
        setTitle(song.name());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(R.string.app_name);
    }
}
