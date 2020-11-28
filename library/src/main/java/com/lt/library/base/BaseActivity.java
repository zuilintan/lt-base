package com.lt.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.viewbinding.ViewBinding;

import com.hunter.library.debug.HunterDebugImpl;
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

    @HunterDebugImpl
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

    @HunterDebugImpl
    @Override
    protected void onStart() {
        super.onStart();
    }

    @HunterDebugImpl
    @Override
    protected void onResume() {
        super.onResume();
    }

    @HunterDebugImpl
    @Override
    protected void onPause() {
        super.onPause();
    }

    @HunterDebugImpl
    @Override
    protected void onStop() {
        super.onStop();
    }

    @HunterDebugImpl
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @HunterDebugImpl
    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeEvent();
        freeData();
        freeView();
        mViewBinding = null;
    }

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplicationContext();
    }

    protected abstract V bindView();//绑定视图(eg: ViewBinding)

    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    }//绑定数据(eg: Bundle, SaveInstanceState, SharedPreferences)

    protected void initView() {
    }//初始化视图

    protected void initData() {
    }//初始化数据

    protected void initEvent() {
    }//初始化事件(eg: OnClickListener)

    protected void saveState(@NonNull Bundle outState) {
    }//存储临时数据(eg: SaveInstanceState)

    protected void freeEvent() {
    }//释放事件(eg: OnClickListener)

    protected void freeData() {
    }//释放数据

    protected void freeView() {
    }//释放视图
}
