package com.example.android.uamp.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.uamp.R;
import com.example.android.uamp.databinding.FragmentSingerBinding;

/**
 * Created by phearom on 7/15/16.
 */
public class SingerFragment extends BaseFragment {
    private FragmentSingerBinding mBinding;

    @Override
    public String getTitle() {
        return "Singer";
    }

    @Override
    public Integer getIcon() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_singer, container, false);
        return mBinding.getRoot();
    }
}
