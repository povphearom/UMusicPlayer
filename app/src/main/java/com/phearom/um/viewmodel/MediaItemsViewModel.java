package com.phearom.um.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

/**
 * Created by phearom on 7/18/16.
 */
public class MediaItemsViewModel extends BaseObservable {
    @Bindable
    public ObservableList<MediaItemViewModel> items;

    public MediaItemsViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(MediaItemViewModel item) {
        items.add(item);
    }

    public void getItem(int pos) throws Exception {
        items.get(pos);
    }
}
