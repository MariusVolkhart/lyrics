package com.volkhart.lyrics;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.google.auto.value.AutoValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@AutoValue
public abstract class Song implements Parcelable {

    public static Song fromJson(InputStream is) throws IOException {
        String name = null;
        String lyrics = null;
        String chords = null;

        try (JsonReader reader = new JsonReader(new InputStreamReader(is))) {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "lyrics":
                        lyrics = reader.nextString();
                        break;
                    case "chords":
                        chords = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                }
            }
        }
        return new AutoValue_Song(name, lyrics, chords);
    }

    public abstract String name();

    public abstract String lyrics();

    @Nullable
    public abstract String chords();
}
