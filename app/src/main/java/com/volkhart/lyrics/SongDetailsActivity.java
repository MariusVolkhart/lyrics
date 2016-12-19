package com.volkhart.lyrics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SongDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_SONG = "song";

    public static Intent newIntent(Context context, Song song) {
        Intent intent = new Intent(context, SongDetailsActivity.class);
        intent.putExtra(EXTRA_SONG, song);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Song song = getIntent().getParcelableExtra(EXTRA_SONG);
            setTitle(song.name());
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, SongFragment.newInstance(song), SongFragment.TAG)
                    .commit();
        }
    }
}
