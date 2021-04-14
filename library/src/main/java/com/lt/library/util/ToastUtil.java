package com.lt.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Toast mToast;
    private String mText;
    private Integer mDuration;
    private Integer mGravity;
    private Integer mOffsetX;
    private Integer mOffsetY;
    private Integer mLayoutRes;
    private Integer mTextViewIdRes;

    private ToastUtil() {
    }

    private ToastUtil(Builder builder) {
        mText = builder.mText;
        mDuration = builder.mDuration;
        mGravity = builder.mGravity;
        mOffsetX = builder.mOffsetX;
        mOffsetY = builder.mOffsetY;
        mLayoutRes = builder.mLayoutRes;
        mTextViewIdRes = builder.mTextViewIdRes;
    }

    @SuppressLint("ShowToast")
    private void execShow() {
        Context context = ContextUtil.getInstance().getApplicationContext();
        if (mToast == null) {
            if (mLayoutRes == -1) {
                mToast = Toast.makeText(context, mText, mDuration);
            } else {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(mLayoutRes, null);

                TextView textView = view.findViewById(mTextViewIdRes);
                textView.setText(mText);
                mToast = new Toast(context);
                mToast.setView(view);
                mToast.setDuration(mDuration);
            }
            mToast.setGravity(mGravity, mOffsetX, mOffsetY);
        }
        mToast.show();
    }

    public ToastUtil show() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            execShow();
        } else {
            mHandler.post(this::execShow);
        }
        return this;
    }

    public ToastUtil cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        return this;
    }

    public void release() {
        mHandler.removeCallbacksAndMessages(null);
        cancel();
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
            mText = ContextUtil.getInstance().getApplicationContext().getString(stringRes);
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
            return new ToastUtil(this);
        }
    }
}
