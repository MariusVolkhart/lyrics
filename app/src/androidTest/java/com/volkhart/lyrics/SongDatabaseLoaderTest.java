package com.volkhart.lyrics;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class SongDatabaseLoaderTest {

    @Test
    public void canLoadSongs() {
        Context context = InstrumentationRegistry.getTargetContext();
        SongDatabaseLoader loader = new SongDatabaseLoader(context);

        assertThat(loader.loadInBackground()).isNotEmpty();
    }
}
