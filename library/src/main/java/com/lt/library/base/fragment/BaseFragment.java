package com.lt.library.base.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewbinding.ViewBinding;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 11:00
 * @版本: 1.0
 * @描述: BaseFragment
 * 1.0: Initial Commit
 * <p>
 * buildFeatures {
 * viewBinding = true
 * }
 */

public abstract class BaseFragment<V extends ViewBinding> extends Fragment {
    protected V mViewBinding;
    private ViewModelProvider mAppViewModelProvider;
    private ViewModelProvider mActivityViewModelProvider;
    private ViewModelProvider mFragmentViewModelProvider;

    @TargetApi(value = Build.VERSION_CODES.M)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = bindView(inflater, container);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData(getArguments(), savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeEvent();
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
    }

    /**
     * 绑定视图 (e.g. ViewBinding)
     *
     * @return ViewBinding
     */
    protected abstract V bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 绑定数据 (e.g. Bundle, SaveInstanceState, SharedPreferences, MMKV)
     *
     * @param arguments          外部传入的数据
     * @param savedInstanceState 已存储的实例状态
     */
    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化视图
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化事件 (e.g. OnClickListener)
     */
    protected void initEvent() {
    }

    /**
     * 显示视图
     */
    protected void showView() {
    }

    /**
     * 隐藏视图
     */
    protected void hideView() {
    }

    /**
     * 存储临时数据 (e.g. SaveInstanceState)
     *
     * @param outState 用以存储实例状态的 Bundle
     */
    protected void saveState(@NonNull Bundle outState) {
    }

    /**
     * 释放事件 (e.g. OnClickListener)
     */
    protected void freeEvent() {
    }

    /**
     * 释放数据
     */
    protected void freeData() {
    }

    /**
     * 释放视图
     */
    protected void freeView() {
    }

    protected Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    /**
     * 获取 Application 级别作用域的 ViewModel
     *
     * @param cls ViewModel 类
     * @param <T> ViewModel
     * @return ViewModel 实例
     */
    protected <T extends ViewModel> T getAppScopeViewModel(@NonNull Class<T> cls) {
        if (mAppViewModelProvider == null) {
            mAppViewModelProvider = new ViewModelProvider((ViewModelStoreOwner) ContextUtil.getContext());
        }
        return mAppViewModelProvider.get(cls);
    }

    /**
     * 获取 Activity 级别作用域的 ViewModel
     *
     * @param cls ViewModel 类
     * @param <T> ViewModel
     * @return ViewModel 实例
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> cls) {
        if (mActivityViewModelProvider == null) {
            mActivityViewModelProvider = new ViewModelProvider(requireActivity());
        }
        return mActivityViewModelProvider.get(cls);
    }

    /**
     * 获取 Fragment 级别作用域的 ViewModel
     *
     * @param cls ViewModel 类
     * @param <T> ViewModel
     * @return ViewModel 实例
     */
    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> cls) {
        if (mFragmentViewModelProvider == null) {
            mFragmentViewModelProvider = new ViewModelProvider(this);
        }
        return mFragmentViewModelProvider.get(cls);
    }

    /**
     * 获取 Fragment 级别作用域的 ViewModel
     *
     * @param cls           ViewModel 类
     * @param <T>           ViewModel
     * @param fragmentClass 目标 Fragment Class
     * @return ViewModel 实例
     */
    protected <T extends ViewModel, F extends Fragment> T getFragmentScopeViewModel(@NonNull Class<T> cls, @NonNull Class<F> fragmentClass) {
        if (mFragmentViewModelProvider == null) {
            mFragmentViewModelProvider = new ViewModelProvider(findFragment(this, fragmentClass));
        }
        return mFragmentViewModelProvider.get(cls);
    }

    /**
     * 查找包含源 Fragment 的父 Fragment 树中, 为目标 Class 的实例
     *
     * @param sourceFragment      此 Fragment
     * @param targetFragmentClass 目标 Fragment Class
     * @param <T>                 Fragment
     * @return Fragment 实例
     */
    protected <T extends Fragment> Fragment findFragment(@NonNull Fragment sourceFragment, @NonNull Class<T> targetFragmentClass) {
        if (targetFragmentClass.isInstance(sourceFragment)) {
            return sourceFragment;
        }
        return findFragment(sourceFragment.requireParentFragment(), targetFragmentClass);
    }
}
