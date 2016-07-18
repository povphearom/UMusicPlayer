package com.phearom.um.binder;

import com.phearom.api.binder.ConditionalDataBinder;
import com.phearom.um.viewmodel.MediaItemViewModel;

/**
 * Created by phearom on 7/18/16.
 */
public class MediaItemBinder extends ConditionalDataBinder<MediaItemViewModel> {
    public MediaItemBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(MediaItemViewModel model) {
        return true;
    }
}
