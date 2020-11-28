package com.lt.library.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.lt.library.util.context.ContextUtil;

import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2018/12/25 17:11
 * @版本: 1.0
 * @描述: //ScreenUtil
 * 源址: https://blog.csdn.net/lmj623565791/article/details/38965311
 * 1.0: Initial Commit
 */

public class ScreenUtil {
    private ScreenUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showActionBar(AppCompatActivity appCompatActivity) {
        Objects.requireNonNull(appCompatActivity.getSupportActionBar())
               .show();
    }//显示ActionBar，setContentView()后调用

    public static void hideActionBar(AppCompatActivity appCompatActivity) {
        Objects.requireNonNull(appCompatActivity.getSupportActionBar())
               .hide();
    }//隐藏ActionBar，setContentView()后调用

    public static void showStatusBar(Activity activity) {
        activity.getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }//显示StatusBar

    public static void hideStatusBar(Activity activity) {
        activity.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }//隐藏StatusBar

    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) ContextUtil.getInstance().getApplicationContext()
                                                                 .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay()
                     .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }//获取屏幕宽度

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) ContextUtil.getInstance().getApplicationContext()
                                                                 .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay()
                     .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }//获取屏幕高度

    public static int getStatusBarWidth() {
        return getScreenWidth();
    }//获取状态栏宽度

    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight() {
        //Plan A
        //Object statusBarHeightObj = ReflectionUtil.getField("com.android.internal.R$dimen", "status_bar_height");
        //int resId = Integer.parseInt(Objects.requireNonNull(statusBarHeightObj).toString());
        //Plan B
        int resId = ContextUtil.getInstance().getApplicationContext()
                               .getResources().getIdentifier("status_bar_height", "dimen", "android");
        return ContextUtil.getInstance().getApplicationContext()
                          .getResources().getDimensionPixelSize(resId);
    }//获取状态栏高度

    public static Drawable snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow()
                            .getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, width, height);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }//获取当前屏幕截图，包含状态栏(但状态栏无内容)

    public static Drawable snapShotWithoutStatusBar(Activity activity) {
        //获取window最顶层的view
        View view = activity.getWindow()
                            .getDecorView();
        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //获取状态栏高度
        Rect rect = new Rect();
        activity.getWindow()
                .getDecorView()
                .getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        //获取屏幕宽高
        int width = getScreenWidth();
        int height = getScreenHeight();
        //移除状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width, height - statusBarHeight);
        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }//获取当前屏幕截图，不含状态栏
}
