package com.phearom.um.server;

/**
 * Created by phearom on 7/18/16.
 */
public abstract class OnResultCallback<T> {
    public abstract void onResult(T result);

    public void onError(Exception e) {
        e.printStackTrace();
    }
}
