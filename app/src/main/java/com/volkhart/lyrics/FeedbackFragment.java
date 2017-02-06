package com.volkhart.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.volkhart.feedback.Configuration;

import org.rm3l.maoni.common.contract.Listener;
import org.rm3l.maoni.common.model.Feedback;
import org.rm3l.maoni.email.MaoniEmailListener;

public final class FeedbackFragment extends com.volkhart.feedback.FeedbackFragment {

    public static final String TAG = FeedbackFragment.class.getSimpleName();
    private Listener listener;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String subject = getString(R.string.feedback_subject);
        listener = new MaoniEmailListener(getActivity(), "text/plain", subject, null, null, new String[]{"marius@volkhart.com"}, null, null);
    }

    @Override
    protected Configuration getConfiguration() {
        String authority = BuildConfig.APPLICATION_ID + ".feedback";
        return new Configuration(authority);
    }

    @Override
    public boolean onSendButtonClicked(Feedback feedback) {
        return listener.onSendButtonClicked(feedback);
    }
}
