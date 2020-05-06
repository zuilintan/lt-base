package com.lt.library.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * @作者: LinTan
 * @日期: 2018/12/29 20:27
 * @版本: 1.0
 * @描述: //LogUtil
 * 源址: http://blog.51cto.com/9098858/2096644
 * 1.0: Initial Commit
 */

public class LogUtil {
    private static final String sTagPrefix = "LogUtil";
    private static boolean sIsEnabled = true;//默认启用LogUtil

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void isEnabled(boolean isEnabled) {
        sIsEnabled = isEnabled;
    }//全局控制是否启用LogUtil

    public static void v(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.v(tag, content, tr);
    }

    public static void d(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.d(tag, content, tr);
    }

    public static void i(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.i(tag, content, tr);
    }

    public static void w(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.w(tag, tr);
    }

    public static void e(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.e(tag, content, tr);
    }

    public static void wtf(String content) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.wtf(tag, content);
    }

    public static void wtf(String content, Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        if (!sIsEnabled) return;
        String tag = generateTag();
        Log.wtf(tag, tr);
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.getDefault(), tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(sTagPrefix) ? tag : sTagPrefix + ":" + tag;
        return tag;
    }//生成Tag，格式: LogUtil:类名.方法名(L:行数)
}
