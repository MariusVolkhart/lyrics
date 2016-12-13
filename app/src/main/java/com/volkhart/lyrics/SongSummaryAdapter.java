package com.volkhart.lyrics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class SongSummaryAdapter extends RecyclerView.Adapter<SongSummaryAdapter.SummaryHolder> {

    private final OnSongClickListener clickListener;
    private List<Song> songs = Collections.emptyList();

    public SongSummaryAdapter(OnSongClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public SongSummaryAdapter.SummaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.v("Creating new view");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song_summary, parent, false);
        return new SummaryHolder(view);
    }

    @Override
    public void onBindViewHolder(SongSummaryAdapter.SummaryHolder holder, int position) {
        Timber.v("Rendering position %d", position);
        Song song = songs.get(position);
        holder.songName.setText(song.name());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
        Timber.v("Got new songs");
    }

    public interface OnSongClickListener {
        void onSongClick(Song song);
    }

    public final class SummaryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.songName)
        TextView songName;

        public SummaryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> clickListener.onSongClick(songs.get(getAdapterPosition())));
        }
    }
}
