package com.phearom.um.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.phearom.um.BR;

/**
 * Created by phearom on 7/18/16.
 */
public class MediaItemViewModel extends BaseObservable {
    private MediaBrowserCompat.MediaItem model;

    private MediaDescriptionCompat descriptionCompat;

    public MediaItemViewModel(MediaBrowserCompat.MediaItem item) {
        this.model = item;
        setDescriptionCompat(item.getDescription());
    }

    public CharSequence getTitle() {
        return getDescriptionCompat().getTitle();
    }

    public Uri getIconUri() {
        return getDescriptionCompat().getIconUri();
    }

    @Bindable
    public MediaDescriptionCompat getDescriptionCompat() {
        return descriptionCompat;
    }

    public void setDescriptionCompat(MediaDescriptionCompat descriptionCompat) {
        this.descriptionCompat = descriptionCompat;
        notifyPropertyChanged(BR.descriptionCompat);
    }
}
