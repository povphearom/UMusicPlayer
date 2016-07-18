package com.phearom.api.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.phearom.api.adapter.BindingRecyclerViewAdapter;
import com.phearom.api.binder.ItemBinder;
import com.phearom.api.handler.ClickHandler;
import com.phearom.api.handler.LongClickHandler;
import com.phearom.api.utils.DividerItemDecoration;
import com.phearom.api.utils.SpacesItemDecoration;

import java.util.Collection;

/**
 * Created By Phearom Pov
 * Class for bind ui with data
 */
public class RecyclerViewBindings {
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;
    private static final int KEY_LOAD_HANDLER = -126;
    private static int visibleThreshold = 5;
    private static boolean isLoading = false;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("clickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, ClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("longClickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, LongClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setLongClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemViewBinder")
    public static <T> void setItemViewBinder(RecyclerView recyclerView, ItemBinder<T> itemViewMapper) {
        try {
            Collection<T> items = (Collection<T>) recyclerView.getTag(KEY_ITEMS);
            ClickHandler<T> clickHandler = (ClickHandler<T>) recyclerView.getTag(KEY_CLICK_HANDLER);
            BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
            if (clickHandler != null) {
                adapter.setClickHandler(clickHandler);
            }

            recyclerView.setAdapter(adapter);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"hasFixedSize", "nestedScroll"}, requireAll = true)
    public static void setOption(RecyclerView recyclerView, boolean hasFixed, boolean nested) {
        recyclerView.setHasFixedSize(hasFixed);
        recyclerView.setNestedScrollingEnabled(nested);
    }

    @BindingAdapter("itemSpace")
    public static void itemDecoration(RecyclerView recyclerView, int space) {
        RecyclerView.ItemDecoration itemDecoration;
        try {
            itemDecoration = new SpacesItemDecoration(recyclerView.getContext().getResources().getDimensionPixelOffset(space));
            recyclerView.addItemDecoration(itemDecoration);
        } catch (Exception e) {
            itemDecoration = new SpacesItemDecoration(space);
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    @BindingAdapter("hasDividerVertical")
    public static void hasDivider(RecyclerView recyclerView, boolean vertical) {
        RecyclerView.ItemDecoration itemDecoration;
        if (vertical) {
            itemDecoration = new
                    DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        } else {
            itemDecoration = new
                    DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL_LIST);
        }
        recyclerView.addItemDecoration(itemDecoration);
    }
}