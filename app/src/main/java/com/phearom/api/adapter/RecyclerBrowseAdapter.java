package com.phearom.api.adapter;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phearom.api.handler.ClickHandler;
import com.phearom.api.handler.LongClickHandler;
import com.phearom.api.holder.MediaItemViewHolder;
import com.phearom.um.R;
import com.phearom.um.utils.MediaIDHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.phearom.um.utils.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;

/**
 * Created by phearom on 7/18/16.
 */
public class RecyclerBrowseAdapter extends RecyclerView.Adapter<MediaItemViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private static final int ITEM_MODEL = -124;
    private List<MediaMetadataCompat> items;
    private LayoutInflater inflater;
    private ClickHandler<MediaMetadataCompat> clickHandler;
    private LongClickHandler<MediaMetadataCompat> longClickHandler;

    private Context mContext;

    public RecyclerBrowseAdapter(Context context) {
        this.mContext = context;
        items = new ObservableArrayList<>();
    }

    public void addItem(MediaMetadataCompat item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public List<MediaMetadataCompat> getItems() {
        return items;
    }

    public void setItems(@Nullable Collection<MediaMetadataCompat> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items != null) {
            this.items = new ArrayList<>();
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(mContext);
        }
        View view = inflater.inflate(R.layout.media_list_item, viewGroup, false);
        return new MediaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaItemViewHolder viewHolder, int position) {
        final MediaBrowserCompat.MediaItem item = createMediaItem(items.get(position));

        int itemState = MediaItemViewHolder.STATE_NONE;
        if (item.isPlayable()) {
            itemState = MediaItemViewHolder.STATE_PLAYABLE;
            MediaControllerCompat controller = ((FragmentActivity) mContext)
                    .getSupportMediaController();
            if (controller != null && controller.getMetadata() != null) {
                String currentPlaying = controller.getMetadata().getDescription().getMediaId();
                String musicId = MediaIDHelper.extractMusicIDFromMediaID(
                        item.getDescription().getMediaId());
                if (currentPlaying != null && currentPlaying.equals(musicId)) {
                    PlaybackStateCompat pbState = controller.getPlaybackState();
                    if (pbState == null ||
                            pbState.getState() == PlaybackStateCompat.STATE_ERROR) {
                        itemState = com.phearom.um.ui.MediaItemViewHolder.STATE_NONE;
                    } else if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                        itemState = com.phearom.um.ui.MediaItemViewHolder.STATE_PLAYING;
                    } else {
                        itemState = com.phearom.um.ui.MediaItemViewHolder.STATE_PAUSED;
                    }
                }
            }
        }

        viewHolder.itemView.setTag(ITEM_MODEL, items.get(position));
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setOnLongClickListener(this);

        viewHolder.Bind(item.getDescription(), itemState);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onClick(View v) {
        if (clickHandler != null) {
            MediaMetadataCompat item = (MediaMetadataCompat) v.getTag(ITEM_MODEL);
            clickHandler.onClick(item, v);
        }
    }

    public MediaBrowserCompat.MediaItem getMediaItem(@NonNull MediaMetadataCompat item) {
        return createMediaItem(item);
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickHandler != null) {
            MediaMetadataCompat item = (MediaMetadataCompat) v.getTag(ITEM_MODEL);
            longClickHandler.onLongClick(item, v);
            return true;
        }
        return false;
    }

    public void setClickHandler(ClickHandler<MediaMetadataCompat> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setLongClickHandler(LongClickHandler<MediaMetadataCompat> clickHandler) {
        this.longClickHandler = clickHandler;
    }

    private MediaBrowserCompat.MediaItem createMediaItem(MediaMetadataCompat metadata) {
        String genre = metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
        String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                metadata.getDescription().getMediaId(), MEDIA_ID_MUSICS_BY_GENRE, genre);
        MediaMetadataCompat copy = new MediaMetadataCompat.Builder(metadata)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                .build();
        return new MediaBrowserCompat.MediaItem(copy.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);

    }

    public void clear() {
        items.clear();
    }
}
