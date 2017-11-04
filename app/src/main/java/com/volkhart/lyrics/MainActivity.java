package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.volkhart.keyboardshortcuts.KeyboardShortcutsFragment;

public final class MainActivity extends AppCompatActivity {

    private boolean showMasterDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMasterDetail = getResources().getBoolean(R.bool.showMasterDetail);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.master, SongIndexFragment.newInstance(), SongIndexFragment.TAG)
                    .add(FeedbackFragment.newInstance(), FeedbackFragment.TAG)
                    .add(KeyboardShortcutsFragment.newInstance(), KeyboardShortcutsFragment.TAG)
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
