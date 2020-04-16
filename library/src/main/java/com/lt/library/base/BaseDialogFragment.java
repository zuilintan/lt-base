package com.lt.library.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lt.library.util.DensityUtil;
import com.lt.library.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

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
    protected WeakReference<FragmentActivity> mActivityWeakReference;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private int mHorizontalMargin = 64;
    private int mWindowAnimations;
    private int mGravity = Gravity.CENTER;
    private float mDimAmount = 0.5F;
    private boolean mIsOutCancel = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityWeakReference = new WeakReference<>((FragmentActivity) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }//Plan A, 不推荐, 一般用于创建替代传统的Dialog对话框的场景, UI简单, 功能单一

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutResId(), container, false);
        initView(new BaseViewHolder(view), this);
        return view;
    }//Plan B, 一般用于创建复杂内容弹窗或全屏展示效果的场景,UI复杂, 功能复杂, 或有网络请求等异步操作

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(mIsOutCancel);
        Window window = getDialog().getWindow();
        if (Objects.nonNull(window)) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (mWindowAnimations > 0) {
                window.setWindowAnimations(mWindowAnimations);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Window activityWindow = mActivityWeakReference.get().getWindow();
        Window dialogWindow = getDialog().getWindow();
        if (Objects.isNull(activityWindow) || Objects.isNull(dialogWindow)) {
            super.onStart();
        } else {
            int activitySystemUiVisibility = activityWindow.getDecorView().getSystemUiVisibility();
            int dialogSystemUiVisibility = dialogWindow.getDecorView().getSystemUiVisibility();
            boolean isDiffVisibility = activitySystemUiVisibility != dialogSystemUiVisibility;
            if (isDiffVisibility) {
                dialogWindow.addFlags(FLAG_NOT_FOCUSABLE);
            }//若Activity与Dialog的SystemUiVisibility不同, 则在回调父类onStart()前主动移除按键输入焦点
            super.onStart();
            if (isDiffVisibility) {
                dialogWindow.getDecorView().setSystemUiVisibility(activitySystemUiVisibility);
                dialogWindow.clearFlags(FLAG_NOT_FOCUSABLE);
            }//若Activity与Dialog的SystemUiVisibility不同, 则在回调父类onStart()后以Activity为准, 统一二者的SystemUiVisibility, 并取回按键输入焦点
            initParam(dialogWindow);
        }
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
        freeView();
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
        manager.beginTransaction()
               .remove(this)
               .commit();//规避并发点击时, IllegalStateException: Fragment already added
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }
    }

    private void initParam(Window dialogWindow) {
        dialogWindow.setLayout(mLayoutWidth > 0
                                       ? DensityUtil.dp2px(mActivityWeakReference.get(), mLayoutWidth)
                                       : ScreenUtil.getScreenWidth(mActivityWeakReference.get())
                                       - 2 * DensityUtil.dp2px(mActivityWeakReference.get(), mHorizontalMargin),
                               mLayoutHeight > 0
                                       ? DensityUtil.dp2px(mActivityWeakReference.get(), mLayoutHeight)
                                       : WindowManager.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(mGravity);
        dialogWindow.setDimAmount(mDimAmount);
    }

    public BaseDialogFragment setLayoutSize(int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        return this;
    }//设置大小

    public BaseDialogFragment setHorizontalMargin(int horizontalMargin) {
        mHorizontalMargin = horizontalMargin;
        return this;
    }//设置水平边距

    public BaseDialogFragment setWindowAnimations(@StyleRes int windowAnimations) {
        mWindowAnimations = windowAnimations;
        return this;
    }//设置过场动画

    public BaseDialogFragment setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }//设置显示位置

    public BaseDialogFragment setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
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

    protected abstract void initView(BaseViewHolder vh, BaseDialogFragment df);

    protected abstract void freeView();
}
