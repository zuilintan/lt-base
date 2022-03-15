package com.lt.library.util;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @作者: LinTan
 * @日期: 2018/12/29 20:27
 * @版本: 1.0
 * @描述: LogUtil
 * 源址: http://blog.51cto.com/9098858/2096644
 * 1.0: Initial Commit
 */

public class LogUtil {
    private static final AtomicBoolean sIsEnabled = new AtomicBoolean(true);//默认启用
    private static final String CLASS_NAME = LogUtil.class.getName();
    private static final String UNK_FLAG = "<unk>";
    private static final String NULL_FLAG = "<null>";
    private static final String EMPTY_FLAG = "<empty>";
    private static final String METHOD_FLAG = "<method>";
    private static final int LARGE_MSG_SEGMENT_LENGTH = 3 * 1024;
    private static String GLOBAL_TAG;

    static {
        GLOBAL_TAG = Application.getProcessName();
    }

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static String formatStdTag(String customTag) {
        String result;
        String fileName = UNK_FLAG;
        int lineNumber = 0;
        String methodName = UNK_FLAG;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTraceElements.length; i++) {
            String className = stackTraceElements[i].getClassName();
            if (!className.equals(LogUtil.CLASS_NAME)) {
                fileName = stackTraceElements[i].getFileName();
                lineNumber = stackTraceElements[i].getLineNumber();
                methodName = stackTraceElements[i].getMethodName();
                int index = methodName.indexOf('$');//判断是否为Lambda表达式
                if (index > -1) {
                    methodName = methodName.substring(0, index + 1);
                }
                break;
            }
        }
        String tagFormat;
        if (TextUtils.isEmpty(customTag)) {
            tagFormat = "%s:(%s:%d):%s";//全局Tag:(文件名:行号):方法名
            result = String.format(Locale.getDefault(), tagFormat, GLOBAL_TAG, fileName, lineNumber, methodName);
        } else {
            tagFormat = "%s:(%s:%d):%s:%s";//全局Tag:(文件名:行号):方法名:自定义Tag
            result = String.format(Locale.getDefault(), tagFormat, GLOBAL_TAG, fileName, lineNumber, methodName, customTag);
        }
        return result;
    }//注意: tag及时间, 进程, 线程号, 日志等级的字数阈值约为128byte, 超出部分不显示(但还是会占用该条Log的可显示字数, 即覆盖msg)

    private static void printlnLog(int logLevel, String tag, String msg, Throwable tr) {
        if (msg == null) {
            printlnLogInternal(logLevel, tag, NULL_FLAG, tr);
            return;
        }
        if (msg.isEmpty()) {
            printlnLogInternal(logLevel, tag, EMPTY_FLAG, tr);
            return;
        }
        if (msg.length() <= LARGE_MSG_SEGMENT_LENGTH) {
            printlnLogInternal(logLevel, tag, msg, tr);
            return;
        }
        while (msg.length() > LARGE_MSG_SEGMENT_LENGTH) {//循环分段打印日志
            printlnLogInternal(logLevel, tag, msg.substring(0, LARGE_MSG_SEGMENT_LENGTH), tr);
            msg = msg.substring(LARGE_MSG_SEGMENT_LENGTH);
        }
        printlnLogInternal(logLevel, tag, msg, tr);//打印剩余日志
    }//注意: Android未限制msg字数阈值, 但限制了 tag + msg 的字数阈值约为4096byte

    private static void printlnLogInternal(int logLevel, String tag, String msg, Throwable tr) {
        switch (logLevel) {
            case Log.VERBOSE:
                if (tr == null) {
                    Log.v(tag, msg);
                } else {
                    Log.v(tag, msg, tr);
                }
                break;
            case Log.DEBUG:
                if (tr == null) {
                    Log.d(tag, msg);
                } else {
                    Log.d(tag, msg, tr);
                }
                break;
            case Log.INFO:
                if (tr == null) {
                    Log.i(tag, msg);
                } else {
                    Log.i(tag, msg, tr);
                }
                break;
            case Log.WARN:
                if (tr == null) {
                    Log.w(tag, msg);
                } else {
                    Log.w(tag, msg, tr);
                }
                break;
            case Log.ERROR:
                if (tr == null) {
                    Log.e(tag, msg);
                } else {
                    Log.e(tag, msg, tr);
                }
                break;
            case Log.ASSERT:
                if (tr == null) {
                    Log.wtf(tag, msg);
                } else {
                    Log.wtf(tag, msg, tr);
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isEnable() {
        return sIsEnabled.get();
    }

    public static void setEnable(boolean isEnabled) {
        sIsEnabled.set(isEnabled);
    }

    public static String getGlobalTag() {
        return GLOBAL_TAG;
    }

    public static void setGlobalTag(String tag) {
        GLOBAL_TAG = tag;
    }

    public static void v() {
        v(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.VERBOSE, formatStdTag(tag), msg, tr);
    }

    public static void d() {
        d(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.DEBUG, formatStdTag(tag), msg, tr);
    }

    public static void i() {
        i(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.INFO, formatStdTag(tag), msg, tr);
    }

    public static void w() {
        w(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.WARN, formatStdTag(tag), msg, tr);
    }

    public static void e() {
        e(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.ERROR, formatStdTag(tag), msg, tr);
    }

    public static void wtf() {
        wtf(null, METHOD_FLAG, null);
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
        if (!isEnable()) {
            return;
        }
        printlnLog(Log.ASSERT, formatStdTag(tag), msg, tr);
    }
}
