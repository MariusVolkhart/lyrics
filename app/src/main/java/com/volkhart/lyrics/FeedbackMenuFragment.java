package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.volkhart.feedback.Feedback;

import org.rm3l.maoni.common.contract.Listener;
import org.rm3l.maoni.email.MaoniEmailListener;

public final class FeedbackMenuFragment extends Fragment {
    public static final String TAG = FeedbackMenuFragment.class.getSimpleName();

    public static FeedbackMenuFragment newInstance() {
        return new FeedbackMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feedback, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                Listener listener = new MaoniEmailListener(getActivity(), "text/plain", "subject", "body header", "body footer", new String[]{"toAddress"}, null, null);
                String authority = BuildConfig.APPLICATION_ID + ".feedback";
                new Feedback.Builder(authority, listener)
                        .build()
                        .start(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
