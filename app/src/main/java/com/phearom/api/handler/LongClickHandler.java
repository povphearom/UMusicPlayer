package com.phearom.api.handler;

import android.view.View;

public interface LongClickHandler<T>
{
    void onLongClick(T viewModel, View v);
}