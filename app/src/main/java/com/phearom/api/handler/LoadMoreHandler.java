package com.phearom.api.handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class LoadMoreHandler extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;
    private int totalItemCount = 0;
    private int lastVisibleItem = 0;
    private boolean isLoading = false;
    private int visibleThreshold = 5;

    public LoadMoreHandler(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    protected abstract void onLoadMore();

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        if (!isLoading) {
            if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                isLoading = true;
                onLoadMore();
            }
        }
    }

    public void setLoaded() {
        this.isLoading = false;
    }
}