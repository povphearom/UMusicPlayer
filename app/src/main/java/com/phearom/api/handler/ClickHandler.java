package com.phearom.api.handler;

import android.view.View;

public interface ClickHandler<T>
{
    void onClick(T viewModel, View v);
}