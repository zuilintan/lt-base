package com.lt.library.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 11:00
 * @版本: 1.0
 * @描述: //BaseFragment
 * 1.0: Initial Commit
 */

public abstract class BaseFragment<A extends FragmentActivity, V extends ViewBinding> extends Fragment {
    protected A mActivity;
    protected V mViewBinding;

    @SuppressWarnings("unchecked")
    @TargetApi(value = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (A) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindData(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = bindView(inflater, container);
        initView();
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        loadTempData(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadLongData();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        showView();
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            showView();
        } else {
            hideView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideView();
    }

    @Override
    public void onStop() {
        super.onStop();
        freeEvent();
        saveLongData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveTempData(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeData();
        freeView();
        mViewBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    protected abstract void bindData(Bundle arguments);

    protected abstract V bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void loadTempData(@Nullable Bundle savedInstanceState);

    protected abstract void loadLongData();

    protected abstract void initEvent();

    protected abstract void showView();

    protected abstract void hideView();

    protected abstract void freeEvent();

    protected abstract void saveLongData();

    protected abstract void saveTempData(@NonNull Bundle outState);

    protected abstract void freeData();

    protected abstract void freeView();
}
