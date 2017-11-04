package com.volkhart.lyrics;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

public final class SongSortedListCallback extends SortedList.Callback<Song> {

    private final RecyclerView.Adapter<SongSummaryAdapter.SummaryHolder> adapter;

    SongSortedListCallback(RecyclerView.Adapter<SongSummaryAdapter.SummaryHolder> adapter) {
        this.adapter = adapter;
    }

    @Override
    public int compare(Song left, Song right) {
        return left.name().compareTo(right.name());
    }

    @Override
    public void onChanged(int position, int count) {
        adapter.notifyItemRangeChanged(position, count);
    }

    @Override
    public boolean areContentsTheSame(Song oldItem, Song newItem) {
        // We can do reference comparison b/c they are immutable!
        return oldItem == newItem;
    }

    @Override
    public boolean areItemsTheSame(Song item1, Song item2) {
        return item1.equals(item2);
    }

    @Override
    public void onInserted(int position, int count) {
        adapter.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }
}
