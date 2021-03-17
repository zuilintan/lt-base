package com.lt.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.lt.library.util.context.ContextUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @作者: LinTan
 * @日期: 2019/1/5 18:02
 * @版本: 1.0
 * @描述: //ToastUtil
 * 源址: https://www.jianshu.com/p/c226225db747
 * 1.0: Initial Commit
 */

public class ToastUtil {
    private static final AtomicBoolean sIsEnabled = new AtomicBoolean(true);//默认启用
    private static Toast sToast;

    @SuppressLint("ShowToast")
    private static Toast createStdToast(Object content, @DurationDef int duration, @GravityDef int gravity) {
        String text = null;
        Context context = ContextUtil.getInstance().getApplicationContext();
        if (content instanceof Integer) {
            text = context.getString((Integer) content);
        } else if (content instanceof String) {
            text = (String) content;
        }
        sToast = Toast.makeText(context, text, duration);
        sToast.setGravity(gravity, 0, 0);//仅初次创建时有效
        return sToast;
    }

    private static void cancel() {
        if (Objects.isNull(sToast)) {
            return;
        }
        sToast.cancel();
        sToast = null;
    }

    public static boolean isEnable() {
        return sIsEnabled.get();
    }

    public static void setEnable(boolean isEnabled) {
        sIsEnabled.set(isEnabled);
    }

    public static void show(@StringRes int stringId) {
        show(stringId, false);
    }

    public static void show(@StringRes int stringId, boolean isImmediate) {
        show(stringId, Toast.LENGTH_SHORT, isImmediate);
    }

    public static void show(String text) {
        show(text, false);
    }

    public static void show(String text, boolean isImmediate) {
        show(text, Toast.LENGTH_SHORT, isImmediate);
    }

    public static void show(@StringRes int stringId, @DurationDef int duration) {
        show(stringId, duration, false);
    }

    public static void show(@StringRes int stringId, @DurationDef int duration, boolean isImmediate) {
        show(stringId, duration, Gravity.CENTER, isImmediate);
    }

    public static void show(String text, @DurationDef int duration) {
        show(text, duration, false);
    }

    public static void show(String text, @DurationDef int duration, boolean isImmediate) {
        show(text, duration, Gravity.CENTER, isImmediate);
    }

    public static void show(@StringRes int stringId, @DurationDef int duration, @GravityDef int gravity) {
        show(stringId, duration, gravity, false);
    }

    public static void show(@StringRes int stringId, @DurationDef int duration, @GravityDef int gravity, boolean isImmediate) {
        if (!isEnable()) {
            return;
        }
        if (isImmediate) {
            cancel();//如果当前Toast没有消失，则取消该Toast
        }
        createStdToast(stringId, duration, gravity).show();
        sToast = null;
    }

    public static void show(String text, @DurationDef int duration, @GravityDef int gravity) {
        show(text, duration, gravity, false);
    }

    public static void show(String text, @DurationDef int duration, @GravityDef int gravity, boolean isImmediate) {
        if (!isEnable()) {
            return;
        }
        if (isImmediate) {
            cancel();//如果当前Toast没有消失，则取消该Toast
        }
        createStdToast(text, duration, gravity).show();
        sToast = null;
    }

    @IntDef({Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface GravityDef {
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    @interface DurationDef {
    }
}
