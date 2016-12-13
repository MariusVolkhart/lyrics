package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.moshi.Moshi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class SongIndexFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Song>>, SongSummaryAdapter.OnSongClickListener {
    public static final String TAG = SongIndexFragment.class.getSimpleName();
    private static final int LOADER_SONGS = 0;
    private static final Moshi moshi = new Moshi.Builder()
            .add(LyricsAdapterFactory.create())
            .build();

    @BindView(R.id.list)
    RecyclerView list;

    private SongSummaryAdapter adapter;

    public static SongIndexFragment newInstance() {
        return new SongIndexFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.v("onCreateView");
        View root = inflater.inflate(R.layout.fragment_song_index, container, false);
        ButterKnife.bind(this, root);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setHasFixedSize(true);
        adapter = new SongSummaryAdapter(this);
        list.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Timber.v("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_SONGS, null, this);
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SONGS:
                Timber.v("Creating song loader");
                return new SongDatabaseLoader(getContext(), moshi);
            default:
                throw new IllegalArgumentException("Loader id " + id + " is not supported");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> data) {
        Timber.v("onLoadFinished. Got the songs.");
        adapter.setSongs(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Song>> loader) {
        // No-op
    }

    @Override
    public void onSongClick(Song song) {
        ((MainActivity) getActivity()).showSong(song);
    }
}
