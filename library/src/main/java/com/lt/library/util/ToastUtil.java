package com.lt.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.lt.library.util.context.ContextUtil;

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
    private static final AtomicBoolean sEnable = new AtomicBoolean(true);//默认启用
    private String mText;
    private Integer mDuration;
    private Integer mGravity;
    private Integer mOffsetX;
    private Integer mOffsetY;
    private Integer mLayoutRes;
    private Integer mTextViewIdRes;

    private ToastUtil() {
    }

    private static ToastUtil getInstance() {
        return ToastUtilHolder.INSTANCE;
    }

    private void setBuilder(Builder builder) {
        mText = builder.mText;
        mDuration = builder.mDuration;
        mGravity = builder.mGravity;
        mOffsetX = builder.mOffsetX;
        mOffsetY = builder.mOffsetY;
        mLayoutRes = builder.mLayoutRes;
        mTextViewIdRes = builder.mTextViewIdRes;
    }

    @SuppressLint("ShowToast")
    private Toast make() {
        if (!sEnable.get()) {
            return null;
        }
        Context context = ContextUtil.getAppContext();
        Toast toast;
        if (mLayoutRes == -1) {
            toast = Toast.makeText(context, mText, mDuration);
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(mLayoutRes, null);
            TextView textView = view.findViewById(mTextViewIdRes);
            textView.setText(mText);
            toast = new Toast(context);
            toast.setView(view);
            toast.setDuration(mDuration);
        }
        toast.setGravity(mGravity, mOffsetX, mOffsetY);
        return toast;
    }

    public void show() {
        if (!sEnable.get()) {
            return;
        }
        Toast make = make();
        make.show();
    }

    @Nullable
    public Toast showNow(@Nullable Toast toast) {
        if (!sEnable.get()) {
            return null;
        }
        Toast make = make();
        cancel(toast);
        make.show();
        return make;
    }

    public void cancel(@Nullable Toast toast) {
        if (!sEnable.get()) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
    }

    private static class ToastUtilHolder {
        private static final ToastUtil INSTANCE = new ToastUtil();
    }

    public static class Builder {
        private String mText;
        private int mDuration = Toast.LENGTH_SHORT;
        private int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        private int mOffsetX = 0;
        private int mOffsetY = 0;
        private int mLayoutRes = -1;
        private int mTextViewIdRes = -1;

        public Builder setText(@StringRes int stringRes) {
            mText = ContextUtil.getAppContext().getString(stringRes);
            return this;
        }

        public Builder setText(String text) {
            mText = text;
            return this;
        }

        public Builder setDuration(int duration) {
            mDuration = duration;
            return this;
        }

        public Builder setGravity(int gravity, int offsetX, int offsetY) {
            mGravity = gravity;
            mOffsetX = offsetX;
            mOffsetY = offsetY;
            return this;
        }

        public Builder setLayout(@LayoutRes int layoutRes, @IdRes int textViewIdRes) {
            mLayoutRes = layoutRes;
            mTextViewIdRes = textViewIdRes;
            return this;
        }

        public ToastUtil build() {
            ToastUtil toastUtil = ToastUtil.getInstance();
            toastUtil.setBuilder(this);
            return toastUtil;
        }
    }
}
