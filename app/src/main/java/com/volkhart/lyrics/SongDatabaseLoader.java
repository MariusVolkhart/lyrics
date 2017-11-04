package com.volkhart.lyrics;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public final class SongDatabaseLoader extends AsyncTaskLoader<List<Song>> {

    SongDatabaseLoader(Context context) {
        super(context);
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
        AssetManager assets = getContext().getAssets();
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

        // Execute in parallel
        try {
            for (String jsonAsset : jsonAssets) {
                executor.submit(() -> {
                    try (InputStream json = assets.open("songs/" + jsonAsset)) {
                        Song song = Song.fromJson(json);
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
