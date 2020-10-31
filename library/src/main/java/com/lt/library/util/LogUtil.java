package com.lt.library.util;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @作者: LinTan
 * @日期: 2018/12/29 20:27
 * @版本: 1.0
 * @描述: //LogUtil, Tag格式: 三级及之后域名:(文件名:行号).方法名():自定义Tag
 * 源址: http://blog.51cto.com/9098858/2096644
 * 1.0: Initial Commit
 */

public class LogUtil {
    private static final AtomicBoolean sIsEnabled = new AtomicBoolean(true);//默认启用

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void v(String msg) {
        v("", msg, null);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, msg, tr);
            }
        }
    }

    public static void d(String msg) {
        d("", msg, null);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, tr);
            }
        }
    }

    public static void i(String msg) {
        i("", msg, null);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, msg, tr);
            }
        }
    }

    public static void w(String msg) {
        w("", msg, null);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, msg, tr);
            }
        }
    }

    public static void e(String msg) {
        e("", msg, null);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, msg, tr);
            }
        }
    }

    public static void wtf(String msg) {
        wtf("", msg, null);
    }

    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (isEnable()) {
            tag = genStdTag(tag);
            if (Objects.isNull(tr)) {
                Log.wtf(tag, msg);
            } else {
                Log.wtf(tag, msg, tr);
            }
        }
    }

    public static boolean isEnable() {
        return sIsEnabled.get();
    }

    public static void setEnable(boolean isEnabled) {
        sIsEnabled.set(isEnabled);
    }

    private static String genStdTag(String customTag) {
        String result;
        String processName = Application.getProcessName();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
        String fileName = stackTraceElement.getFileName();
        int lineNumber = stackTraceElement.getLineNumber();
        String methodName = stackTraceElement.getMethodName();
        String domain3rd = processName.substring(processName.indexOf(".", processName.indexOf(".") + 1) + 1);
        String tagFormat;
        if (TextUtils.isEmpty(customTag)) {
            tagFormat = "%s:(%s:%d).%s()";//三级及之后域名:(文件名:行号).方法名()
            result = String.format(Locale.getDefault(), tagFormat, domain3rd, fileName, lineNumber, methodName);
        } else {
            tagFormat = "%s:(%s:%d).%s():%s";//三级及之后域名:(文件名:行号).方法名():自定义Tag
            result = String.format(Locale.getDefault(), tagFormat, domain3rd, fileName, lineNumber, methodName, customTag);
        }
        return result;
    }
}
