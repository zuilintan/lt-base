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
    private static final String PROCESS_NAME = Application.getProcessName();
    private static final String CLASS_NAME = LogUtil.class.getName();

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void v(String msg) {
        v(null, msg, null);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        v(null, msg, tr);
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
        d(null, msg, null);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String msg, Throwable tr) {
        d(null, msg, tr);
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
        i(null, msg, null);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String msg, Throwable tr) {
        i(null, msg, tr);
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
        w(null, msg, null);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String msg, Throwable tr) {
        w(null, msg, tr);
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
        e(null, msg, null);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String msg, Throwable tr) {
        e(null, msg, tr);
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
        wtf(null, msg, null);
    }

    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    public static void wtf(String msg, Throwable tr) {
        wtf(null, msg, tr);
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
        String fileName = "<unknown>";
        int lineNumber = -1;
        String methodName = "<unknown>";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTraceElements.length; i++) {
            String className = stackTraceElements[i].getClassName();
            if (!className.equals(LogUtil.CLASS_NAME)) {
                fileName = stackTraceElements[i].getFileName();
                lineNumber = stackTraceElements[i].getLineNumber();
                methodName = stackTraceElements[i].getMethodName();
                int index = methodName.indexOf('$');
                if (index > -1) {
                    methodName = methodName.substring(0, index + 1);
                }
                break;
            }
        }
        String tagFormat;
        if (TextUtils.isEmpty(customTag)) {
            tagFormat = "%s:(%s:%d).%s()";//进程名:(文件名:行号).方法名()
            result = String.format(Locale.getDefault(), tagFormat, PROCESS_NAME, fileName, lineNumber, methodName);
        } else {
            tagFormat = "%s:(%s:%d).%s():%s";//进程名:(文件名:行号).方法名():自定义Tag
            result = String.format(Locale.getDefault(), tagFormat, PROCESS_NAME, fileName, lineNumber, methodName, customTag);
        }
        return result;
    }
}
