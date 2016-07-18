package com.phearom.api.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phearom.um.R;

public class MediaItemViewHolder extends RecyclerView.ViewHolder {

    public static final int STATE_INVALID = -1;
    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;

    private static ColorStateList sColorStatePlaying;
    private static ColorStateList sColorStateNotPlaying;

    ImageView mImageView;
    TextView mTitleView;
    TextView mDescriptionView;

    private Context mContext;

    public MediaItemViewHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        mImageView = (ImageView) itemView.findViewById(R.id.play_eq);
        mTitleView = (TextView) itemView.findViewById(R.id.title);
        mDescriptionView = (TextView) itemView.findViewById(R.id.description);
    }

    public void Bind(MediaDescriptionCompat description, int state) {
        if (sColorStateNotPlaying == null || sColorStatePlaying == null) {
            initializeColorStateLists(mContext);
        }

        Integer cachedState = STATE_INVALID;
        if (null != itemView)
            cachedState = (Integer) itemView.getTag(R.id.tag_mediaitem_state_cache);

        mTitleView.setText(description.getTitle());
        mDescriptionView.setText(description.getSubtitle());

        if (cachedState == null || cachedState != state) {
            switch (state) {
                case STATE_PLAYABLE:
                    Drawable pauseDrawable = ContextCompat.getDrawable(mContext,
                            R.drawable.ic_play_arrow_black_36dp);
                    DrawableCompat.setTintList(pauseDrawable, sColorStateNotPlaying);
                    mImageView.setImageDrawable(pauseDrawable);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                case STATE_PLAYING:
                    AnimationDrawable animation = (AnimationDrawable)
                            ContextCompat.getDrawable(mContext, R.drawable.ic_equalizer_white_36dp);
                    DrawableCompat.setTintList(animation, sColorStatePlaying);
                    mImageView.setImageDrawable(animation);
                    mImageView.setVisibility(View.VISIBLE);
                    animation.start();
                    break;
                case STATE_PAUSED:
                    Drawable playDrawable = ContextCompat.getDrawable(mContext,
                            R.drawable.ic_equalizer1_white_36dp);
                    DrawableCompat.setTintList(playDrawable, sColorStatePlaying);
                    mImageView.setImageDrawable(playDrawable);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                default:
                    mImageView.setVisibility(View.GONE);
            }
            itemView.setTag(R.id.tag_mediaitem_state_cache, state);
        }
    }

    static private void initializeColorStateLists(Context ctx) {
        sColorStateNotPlaying = ColorStateList.valueOf(ctx.getResources().getColor(
                R.color.media_item_icon_not_playing));
        sColorStatePlaying = ColorStateList.valueOf(ctx.getResources().getColor(
                R.color.media_item_icon_playing));
    }
}
