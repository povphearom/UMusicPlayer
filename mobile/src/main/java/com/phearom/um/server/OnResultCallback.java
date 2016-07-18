package com.phearom.um.server;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

/**
 * Created by phearom on 7/18/16.
 */
public abstract class OnResultCallback<T> {
    public abstract void onResult(T result);

    public void onError(Exception e) {
        e.printStackTrace();
    }
}
