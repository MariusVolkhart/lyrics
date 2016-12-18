package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.moshi.Moshi;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    private List<Song> songs;

    public static SongIndexFragment newInstance() {
        return new SongIndexFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.v("onCreateView");
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.song_index, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() && adapter.getItemCount() == songs.size()) {
                    return false;
                }
                List<Song> filteredModelList = filter(songs, newText);
                adapter.setSongs(filteredModelList);
                list.scrollToPosition(0);
                return true;
            }
        });
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
        songs = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Song>> loader) {
        // No-op
    }

    @Override
    public void onSongClick(Song song) {
        ((MainActivity) getActivity()).showSong(song);
    }

    private List<Song> filter(List<Song> songs, String query) {
        List<Song> filtered = new LinkedList<>();
        for (Song song : songs) {
            if (song.lyrics().toLowerCase(Locale.getDefault()).contains(query)
                    || song.name().toLowerCase(Locale.getDefault()).contains(query)) {
                filtered.add(song);
            }
        }
        return filtered;
    }
}
