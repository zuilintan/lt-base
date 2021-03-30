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
 * @描述: //LogUtil, Tag格式: 三级及之后域名:(文件名:行号).方法名():自定义Tag
 * 源址: http://blog.51cto.com/9098858/2096644
 * 1.0: Initial Commit
 */

public class LogUtil {
    private static final AtomicBoolean sIsEnabled = new AtomicBoolean(true);//默认启用
    private static final String PROCESS_NAME = Application.getProcessName();
    private static final String CLASS_NAME = LogUtil.class.getName();
    private static final String UNK_FLAG = "<unk>";
    private static final String NULL_FLAG = "<null>";
    private static final String EMPTY_FLAG = "<empty>";
    private static final String METHOD_FLAG = "<method>";

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static String createStdTag(String customTag) {
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
    }//注意: tag及时间, 进程, 线程号, 日志等级的字数阈值约为128byte, 超出部分不显示(但还是会占用该条Log的可显示字数, 即覆盖msg)

    private static String createStdMsg(String msg) {
        String result;
        if (msg == null) {
            result = NULL_FLAG;
        } else if (msg.isEmpty()) {
            result = EMPTY_FLAG;
        } else {
            result = msg;
        }
        return result;
    }//注意: Android未限制msg字数阈值, 但限制了一条Log的字数阈值约为4096byte

    public static boolean isEnable() {
        return sIsEnabled.get();
    }

    public static void setEnable(boolean isEnabled) {
        sIsEnabled.set(isEnabled);
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.v(tag, createStdMsg(msg));
        } else {
            Log.v(tag, createStdMsg(msg), tr);
        }
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.d(tag, createStdMsg(msg));
        } else {
            Log.d(tag, createStdMsg(msg), tr);
        }
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.i(tag, createStdMsg(msg));
        } else {
            Log.i(tag, createStdMsg(msg), tr);
        }
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.w(tag, createStdMsg(msg));
        } else {
            Log.w(tag, createStdMsg(msg), tr);
        }
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.e(tag, createStdMsg(msg));
        } else {
            Log.e(tag, createStdMsg(msg), tr);
        }
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
        tag = createStdTag(tag);
        if (tr == null) {
            Log.wtf(tag, createStdMsg(msg));
        } else {
            Log.wtf(tag, createStdMsg(msg), tr);
        }
    }
}
