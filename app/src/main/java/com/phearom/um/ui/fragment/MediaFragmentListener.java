package com.phearom.um.ui.fragment;

import android.support.v4.media.MediaBrowserCompat;

import com.phearom.um.ui.MediaBrowserProvider;

public interface MediaFragmentListener extends MediaBrowserProvider {
        void onMediaItemSelected(MediaBrowserCompat.MediaItem item);

        void setToolbarTitle(CharSequence title);
    }