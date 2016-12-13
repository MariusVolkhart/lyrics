package com.volkhart.lyrics;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.AsyncTaskLoader;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import timber.log.Timber;

public final class SongDatabaseLoader extends AsyncTaskLoader<List<Song>> {

    private final AssetManager assets;
    private final Moshi moshi;

    public SongDatabaseLoader(Context context, Moshi moshi) {
        super(context);
        assets = context.getAssets();
        this.moshi = moshi;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public List<Song> loadInBackground() {
        try {
            String[] jsonAssets = assets.list("songs");
            Timber.v("Loading %d songs", jsonAssets.length);
            ArrayList<Song> songs = new ArrayList<>(jsonAssets.length);
            for (String jsonAsset : jsonAssets) {
                BufferedSource json = Okio.buffer(Okio.source(assets.open("songs/" + jsonAsset)));
                Song song = moshi.adapter(Song.class).fromJson(json);
                songs.add(song);
                Timber.v("Parsed %s", jsonAsset);
            }
            Collections.sort(songs, (left, right) -> left.name().compareTo(right.name()));
            return songs;
        } catch (IOException e) {
            Timber.e(e, "Unable to load assets");
            return Collections.emptyList();
        }
    }
}
