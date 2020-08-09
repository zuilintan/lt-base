package com.lt.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.viewbinding.ViewBinding;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 11:00
 * @版本: 1.0
 * @描述: //BaseActivity
 * 1.0: Initial Commit
 */

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {
    protected V mViewBinding;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindData(getIntent().getExtras());
        mViewBinding = bindView();
        setContentView(mViewBinding.getRoot());
        mContext = this;
        initView();
        initData();
        loadTempData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLongData();
        initEvent();
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
        freeEvent();
        saveLongData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveTempData(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeData();
        freeView();
        mViewBinding = null;
        mContext = null;
    }

    protected Context getUiContext() {
        return mContext;
    }

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    protected abstract void bindData(Bundle arguments);//绑定数据(eg: bundle)

    protected abstract V bindView();//绑定视图(eg: ViewBinding)

    protected abstract void initView();//初始化视图

    protected abstract void initData();//初始化数据

    protected abstract void loadTempData(@Nullable Bundle savedInstanceState);//恢复临时数据(eg: saveInstanceState)

    protected abstract void loadLongData();//恢复持久化数据(eg: SharedPreference)

    protected abstract void initEvent();//初始化事件(eg: OnClickListener)

    protected abstract void freeEvent();//释放事件(eg: OnClickListener)

    protected abstract void saveLongData();//存储持久化数据(eg: SharedPreference)

    protected abstract void saveTempData(@NonNull Bundle outState);//存储临时数据(eg: saveInstanceState)

    protected abstract void freeData();//释放数据

    protected abstract void freeView();//释放视图
}
