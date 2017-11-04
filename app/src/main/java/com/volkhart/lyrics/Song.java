package com.volkhart.lyrics;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Song implements Parcelable {

    public static JsonAdapter<Song> jsonAdapter(Moshi moshi) {
        return new AutoValue_Song.MoshiJsonAdapter(moshi);
    }

    public abstract String name();

    public abstract String lyrics();

    @Nullable
    public abstract String chords();
}
