package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class SongFragment extends Fragment {
    public static final String TAG = SongFragment.class.getSimpleName();
    private static final String ARG_SONG = "song";

    @BindView(R.id.songText)
    TextView songText;
    @BindView(R.id.chords)
    TextView chords;

    public static SongFragment newInstance(Song song) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(ARG_SONG, song);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_song, container, false);
        ButterKnife.bind(this, root);
        Song song = getArguments().getParcelable(ARG_SONG);
        songText.setText(song.lyrics());
        chords.setVisibility(song.chords() == null ? View.GONE : View.VISIBLE);
        chords.setText(song.chords());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
