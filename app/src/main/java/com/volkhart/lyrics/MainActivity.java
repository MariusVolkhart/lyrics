package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public final class MainActivity extends AppCompatActivity {

    private boolean showMasterDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMasterDetail = getResources().getBoolean(R.bool.showMasterDetail);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (BuildConfig.DEBUG) {
                Timber.plant(new Timber.DebugTree());
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.master, SongIndexFragment.newInstance(), SongIndexFragment.TAG)
                    .add(FeedbackMenuFragment.newInstance(), FeedbackMenuFragment.TAG)
                    .commit();
        }
    }

    public void showSong(Song song) {
        if (showMasterDetail) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail, SongFragment.newInstance(song), SongFragment.TAG)
                    .commit();
        } else {
            startActivity(SongDetailsActivity.newIntent(this, song));
        }
    }
}
