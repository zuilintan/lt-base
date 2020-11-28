package com.lt.library.util;

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

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(@StringRes int stringId) {
        if (isEnable()) {
            genStdToast(stringId, Gravity.CENTER, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(String string) {
        if (isEnable()) {
            genStdToast(string, Gravity.CENTER, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(@StringRes int stringId, @GravityDef int gravity) {
        if (isEnable()) {
            genStdToast(stringId, gravity, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(String string, @GravityDef int gravity) {
        if (isEnable()) {
            genStdToast(string, gravity, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(@StringRes int stringId, @GravityDef int gravity, @DurationDef int duration) {
        if (isEnable()) {
            genStdToast(stringId, gravity, duration).show();
        }
    }

    public static void show(String string, @GravityDef int gravity, @DurationDef int duration) {
        if (isEnable()) {
            genStdToast(string, gravity, duration).show();
        }
    }

    public static void cancel() {
        if (isEnable() && Objects.nonNull(sToast)) {
            sToast.cancel();
            sToast = null;
        }
    }

    public static boolean isEnable() {
        return sIsEnabled.get();
    }

    public static void setEnable(boolean isEnabled) {
        sIsEnabled.set(isEnabled);
    }

    private static Toast genStdToast(Object object, @GravityDef int gravity, @DurationDef int duration) {
        String text = null;
        String typeName = object.getClass().getSimpleName();
        switch (typeName) {
            case "String":
                text = (String) object;
                break;
            case "Integer":
                text = ContextUtil.getInstance().getApplicationContext().getString((Integer) object);
                break;
            default:
                break;
        }
        if (Objects.isNull(text)) {
            text = "";
        }
        cancel();//如果当前Toast没有消失，则取消该Toast
        sToast = Toast.makeText(ContextUtil.getInstance().getApplicationContext(), text, duration);
        sToast.setGravity(gravity, 0, 0);//仅初次创建时有效
        return sToast;
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
