package com.volkhart.lyrics;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.AsyncTaskLoader;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;
import okio.Okio;
import timber.log.Timber;

public final class SongDatabaseLoader extends AsyncTaskLoader<List<Song>> {

    private final AssetManager assets;
    private final Moshi moshi;

    SongDatabaseLoader(Context context, Moshi moshi) {
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
        String[] jsonAssets;
        try {
            jsonAssets = assets.list("songs");
        } catch (IOException e) {
            Timber.e(e, "Unable to load assets");
            return Collections.emptyList();
        }
        Timber.v("Loading %d songs", jsonAssets.length);
        List<Song> songs = Collections.synchronizedList(new ArrayList<>(jsonAssets.length));

        // Setup the parallel stuff
        int threadCount = Runtime.getRuntime().availableProcessors();
        ThreadFactory threadFactory = runnable -> new Thread(runnable, "SongLoadThread");
        ExecutorService executor = Executors.newFixedThreadPool(threadCount, threadFactory);
        JsonAdapter<Song> songAdapter = moshi.adapter(Song.class);

        // Execute in parallel
        try {
            for (String jsonAsset : jsonAssets) {
                executor.submit(() -> {
                    try {
                        BufferedSource json = Okio.buffer(Okio.source(assets.open("songs/" + jsonAsset)));
                        Song song = songAdapter.fromJson(json);
                        songs.add(song);
                        Timber.v("Parsed %s", jsonAsset);
                    } catch (IOException e) {
                        Timber.e(e, "Failed to load song: %s", jsonAsset);
                    }
                });
            }
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Timber.w(e, "Failed to wait for song loading to complete");
            }
        }

        Collections.sort(songs, (left, right) -> left.name().compareTo(right.name()));
        return songs;
    }
}
