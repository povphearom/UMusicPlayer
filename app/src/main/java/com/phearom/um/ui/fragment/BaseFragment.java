package com.phearom.um.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by phearom on 7/15/16.
 */
public abstract class BaseFragment extends Fragment {
    public abstract String getTitle();

    public abstract Integer getIcon();

    public abstract String getMediaId();

    public abstract void onConnected();

    public void refresh() {
    }

    ;
}
