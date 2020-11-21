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

    private ContextUtil() {
        mContext = ContextProvider.sContext;
    }

    public static ContextUtil getInstance() {
        return ContextProviderUtilHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    public Application getApplication() {
        return (Application) mContext.getApplicationContext();
    }

    private static class ContextProviderUtilHolder {
        @SuppressLint("StaticFieldLeak")
        private static final ContextUtil INSTANCE = new ContextUtil();
    }
}