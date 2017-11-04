package com.volkhart.lyrics;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

@MoshiAdapterFactory
abstract class LyricsAdapterFactory implements JsonAdapter.Factory {
    static JsonAdapter.Factory create() {
        return new AutoValueMoshi_LyricsAdapterFactory();
    }
}
