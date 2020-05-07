package com.lt.library.util;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.lt.library.util.context.ContextUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @作者: LinTan
 * @日期: 2019/1/5 18:02
 * @版本: 1.0
 * @描述: //ToastUtil
 * 源址: https://www.jianshu.com/p/c226225db747
 * 1.0: Initial Commit
 */

public class ToastUtil {
    private static Toast sToast;
    private static boolean sIsEnabled = true;//默认启用LogUtil

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void isEnabled(boolean isEnabled) {
        sIsEnabled = isEnabled;
    }//全局控制是否启用ToastUtil

    public static void show(@StringRes int stringId) {
        if (!sIsEnabled) return;
        generateToast(stringId, Gravity.CENTER, Toast.LENGTH_SHORT).show();
    }

    public static void show(String string) {
        generateToast(string, Gravity.CENTER, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int stringId, @GravityDef int gravity) {
        generateToast(stringId, gravity, Toast.LENGTH_SHORT).show();
    }

    public static void show(String string, @GravityDef int gravity) {
        generateToast(string, gravity, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int stringId, @GravityDef int gravity, @DurationDef int duration) {
        generateToast(stringId, gravity, duration).show();
    }

    public static void show(String string, @GravityDef int gravity, @DurationDef int duration) {
        generateToast(string, gravity, duration).show();
    }

    public static void cancel() {
        if (sIsEnabled && sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    @SuppressLint("ShowToast")
    private static Toast generateToast(Object object, @GravityDef int gravity, @DurationDef int duration) {
        String text = null;
        String typeName = object.getClass().getSimpleName();
        switch (typeName) {
            case "String":
                text = (String) object;
                break;
            case "Integer":
                text = ContextUtil.getInstance().getContext().getString((Integer) object);
                break;
            default:
                break;
        }
        if (text == null) {
            text = "";
        }
        cancel();//如果当前Toast没有消失，则取消该Toast
        sToast = Toast.makeText(ContextUtil.getInstance().getContext(), text, duration);
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
