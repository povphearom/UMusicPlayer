package com.phearom.api.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.phearom.um.R;
import com.phearom.um.ui.MediaItemViewHolder;
import com.phearom.um.utils.MediaIDHelper;

import java.util.ArrayList;

public class BrowseAdapter extends ArrayAdapter<MediaBrowserCompat.MediaItem> {
    public BrowseAdapter(Activity context) {
        super(context, R.layout.media_list_item, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MediaBrowserCompat.MediaItem item = getItem(position);
        int itemState = MediaItemViewHolder.STATE_NONE;
        if (item.isPlayable()) {
            itemState = MediaItemViewHolder.STATE_PLAYABLE;
            MediaControllerCompat controller = ((FragmentActivity) getContext())
                    .getSupportMediaController();
            if (controller != null && controller.getMetadata() != null) {
                String currentPlaying = controller.getMetadata().getDescription().getMediaId();
                String musicId = MediaIDHelper.extractMusicIDFromMediaID(
                        item.getDescription().getMediaId());
                if (currentPlaying != null && currentPlaying.equals(musicId)) {
                    PlaybackStateCompat pbState = controller.getPlaybackState();
                    if (pbState == null ||
                            pbState.getState() == PlaybackStateCompat.STATE_ERROR) {
                        itemState = MediaItemViewHolder.STATE_NONE;
                    } else if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                        itemState = MediaItemViewHolder.STATE_PLAYING;
                    } else {
                        itemState = MediaItemViewHolder.STATE_PAUSED;
                    }
                }
            }
        }
        return MediaItemViewHolder.setupView(getContext(), convertView, parent,
                item.getDescription(), itemState);
    }
}