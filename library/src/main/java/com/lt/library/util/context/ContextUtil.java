package com.lt.library.util.context;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @作者: LinTan
 * @日期: 2020/4/20 16:03
 * @版本: 1.0
 * @描述: //ContextUtil, 注意在AndroidManifest.xml中注册
 * 1.0: Initial Commit
 * <p>
 * <provider
 * * * android:name=".util.ContextUtil$ContextProvider"
 * * * android:authorities="${applicationId}.contextprovider"
 * * * android:exported="false" />
 */

public class ContextUtil {
    private final Context mContext;
    private final Context mAppContext;

    private ContextUtil() {
        mContext = ContextProvider.sContext;
        mAppContext = ContextProvider.sContext.getApplicationContext();
    }

    private static ContextUtil getInstance() {
        return ContextProviderUtilHolder.INSTANCE;
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public static Context getAppContext() {
        return getInstance().mAppContext;
    }

    public static Application getApp() {
        return (Application) getInstance().mAppContext;
    }

    private static class ContextProviderUtilHolder {
        @SuppressLint("StaticFieldLeak")
        private static final ContextUtil INSTANCE = new ContextUtil();
    }
}