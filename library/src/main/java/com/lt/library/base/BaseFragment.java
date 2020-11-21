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

import com.hunter.library.debug.HunterDebugImpl;
import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 11:00
 * @版本: 1.0
 * @描述: //BaseFragment
 * 1.0: Initial Commit
 */

public abstract class BaseFragment<V extends ViewBinding> extends Fragment {
    protected V mViewBinding;
    protected FragmentActivity mActivity;

    @HunterDebugImpl
    @TargetApi(value = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @HunterDebugImpl
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @HunterDebugImpl
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = bindView(inflater, container);
        return mViewBinding.getRoot();
    }

    @HunterDebugImpl
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData(getArguments(), savedInstanceState);
        initView();
        initData();
        initEvent();
        showView();
    }

    @HunterDebugImpl
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @HunterDebugImpl
    @Override
    public void onStart() {
        super.onStart();
    }

    @HunterDebugImpl
    @Override
    public void onResume() {
        super.onResume();
    }

    @HunterDebugImpl
    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            showView();
        } else {
            hideView();
        }
    }

    @HunterDebugImpl
    @Override
    public void onPause() {
        super.onPause();
    }

    @HunterDebugImpl
    @Override
    public void onStop() {
        super.onStop();
    }

    @HunterDebugImpl
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @HunterDebugImpl
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeEvent();
        freeData();
        freeView();
        mViewBinding = null;
    }

    @HunterDebugImpl
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @HunterDebugImpl
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    protected abstract V bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    }//绑定数据(eg: Bundle, SaveInstanceState, SharedPreferences)

    protected void initView() {
    }//初始化视图

    protected void initData() {
    }//初始化数据

    protected void initEvent() {
    }//初始化事件(eg: OnClickListener)

    protected void showView() {
    }

    protected void hideView() {
    }

    protected void saveState(@NonNull Bundle outState) {
    }//存储临时数据(eg: SaveInstanceState)

    protected void freeEvent() {
    }//释放事件(eg: OnClickListener)

    protected void freeData() {
    }//释放数据

    protected void freeView() {
    }//释放视图
}
