package com.lt.library.base.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewbinding.ViewBinding;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 11:00
 * @版本: 1.0
 * @描述: BaseActivity
 * 1.0: Initial Commit
 * <p>
 * buildFeatures {
 * viewBinding = true
 * }
 */

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {
    protected V mViewBinding;
    private ViewModelProvider mAppViewModelProvider;
    private ViewModelProvider mActivityViewModelProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = bindView();
        setContentView(mViewBinding.getRoot());
        bindData(getIntent().getExtras(), savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeEvent();
        freeData();
        freeView();
        mViewBinding = null;
    }

    /**
     * 绑定视图 (e.g. ViewBinding)
     *
     * @return ViewBinding
     */
    protected abstract V bindView();

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
            mActivityViewModelProvider = new ViewModelProvider(this);
        }
        return mActivityViewModelProvider.get(cls);
    }
}
