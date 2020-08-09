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

    private KeyboardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void openKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) ContextUtil.getInstance().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void closeKeyboard(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) ContextUtil.getInstance().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
