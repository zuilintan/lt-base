package com.lt.library.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lt.library.util.DensityUtil;
import com.lt.library.util.ScreenUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @作者: LinTan
 * @日期: 2019/5/15 10:23
 * @版本: 1.0
 * @描述: //DialogFragment的封装类。
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public abstract class BaseDialogFragment extends DialogFragment {
    protected Context mContext;
    private int mLayoutResId;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private int mHorizontalMargin;
    private int mWindowAnimations;
    private int mGravity = Gravity.CENTER;
    private float mDimAmount = 0.5F;
    private boolean mIsOutCancel = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutResId = setLayoutResId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initWindow();
        View view = inflater.inflate(mLayoutResId, container);
        initView(BaseViewHolder.newInstance(view), this);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        manager.beginTransaction().remove(this).commit();//规避并发点击时, IllegalStateException: Fragment already added
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }
    }

    private void initWindow() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(mLayoutWidth != 0 ? DensityUtil.dp2px(mContext, mLayoutWidth) : ScreenUtil.getScreenWidth(mContext) - 2 * DensityUtil.dp2px(mContext, mHorizontalMargin),
                             mLayoutHeight != 0 ? DensityUtil.dp2px(mContext, mLayoutHeight) : WRAP_CONTENT);
            window.setGravity(mGravity);
            window.setDimAmount(mDimAmount);
            window.setWindowAnimations(mWindowAnimations);
        }
        setCancelable(mIsOutCancel);
    }

    public BaseDialogFragment setLayoutSize(int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        return this;
    }//设置大小

    public BaseDialogFragment setHorizontalMargin(int horizontalMargin) {
        mHorizontalMargin = horizontalMargin;
        return this;
    }//设置水平外边距

    public BaseDialogFragment setWindowAnimations(@StyleRes int windowAnimations) {
        mWindowAnimations = windowAnimations;
        return this;
    }//设置过场动画

    public BaseDialogFragment setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }//设置显示位置

    public BaseDialogFragment setDimAmout(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }//设置外围暗度

    public BaseDialogFragment setOutCancel(boolean isOutCancel) {
        mIsOutCancel = isOutCancel;
        return this;
    }//设置外部取消

    public void show(FragmentManager manager) {
        this.show(manager, getClass().getSimpleName());
    }//显示Dialog

    protected abstract int setLayoutResId();

    protected abstract void initView(BaseViewHolder vh, BaseDialogFragment dialog);
}
