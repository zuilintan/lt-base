package com.lt.library.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:58
 * @版本: 1.0
 * @描述: //KeyboardUtil
 * 1.0: Initial Commit
 */
public class KeyboardUtil {
    private static Runnable sRunnable;

    private KeyboardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 显示键盘
     *
     * @param editText 必须visible, 且所在布局已加载完成
     */
    public static void show(EditText editText) {
        show(editText, 0);
    }

    /**
     * @param editText    必须visible, 且所在布局已加载完成
     * @param delayMillis 延期时长; 因布局未加载完成时(onCreate, onResume, onAttachedToWindow), showSoftInput执行无效, 所以此处可选延时执行
     */
    public static void show(EditText editText, long delayMillis) {
        InputMethodManager inputMethodManager = (InputMethodManager) ContextUtil.getInstance()
                                                                                .getApplicationContext()
                                                                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (delayMillis == 0) {
            editText.requestFocus();
            inputMethodManager.showSoftInput(editText, 0);
        } else {
            postDelayed(editText, delayMillis, inputMethodManager);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param editText 输入框
     */
    public static void hide(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) ContextUtil.getInstance()
                                                                                .getApplicationContext()
                                                                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        postRemoved(editText);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 切换键盘显隐状态
     * 若已显示, 则隐藏; 反之, 则显示
     */
    public static void toggle() {
        InputMethodManager inputMethodManager = (InputMethodManager) ContextUtil.getInstance()
                                                                                .getApplicationContext()
                                                                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, 0);
    }

    private static void postDelayed(EditText editText, long delayMillis, InputMethodManager inputMethodManager) {
        sRunnable = () -> {
            editText.requestFocus();
            inputMethodManager.showSoftInput(editText, 0);
            postRemoved(editText);
        };
        editText.postDelayed(sRunnable, delayMillis);
    }

    private static void postRemoved(EditText editText) {
        editText.removeCallbacks(sRunnable);
        sRunnable = null;
    }
}
