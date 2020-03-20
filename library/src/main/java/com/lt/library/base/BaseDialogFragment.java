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

import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
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
    protected Context mContext;
    private int mLayoutWidth;
    private int mLayoutHeight;
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
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }//Plan A, 不推荐, 一般用于创建替代传统的Dialog对话框的场景, UI简单, 功能单一

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initWindow();
        View view = inflater.inflate(setLayoutResId(), container);
        initView(BaseViewHolder.newInstance(view), this);
        return view;
    }//Plan B, 一般用于创建复杂内容弹窗或全屏展示效果的场景,UI复杂, 功能复杂, 或有网络请求等异步操作

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Window activityWindow = Objects.requireNonNull(getActivity().getWindow());
        Window dialogWindow = Objects.requireNonNull(getDialog().getWindow());
        boolean isSystemUiVisibilitySame = activityWindow.getDecorView().getSystemUiVisibility() == dialogWindow.getDecorView().getSystemUiVisibility();
        if (!isSystemUiVisibilitySame) {
            dialogWindow.addFlags(FLAG_NOT_FOCUSABLE);
        }//若Activity与Dialog的SystemUiVisibility不同, 则在回调父类onStart()前主动移除按键输入焦点
        super.onStart();
        if (!isSystemUiVisibilitySame) {
            dialogWindow.getDecorView().setSystemUiVisibility(activityWindow.getDecorView().getSystemUiVisibility());
            dialogWindow.clearFlags(FLAG_NOT_FOCUSABLE);
        }//若Activity与Dialog的SystemUiVisibility不同, 则在回调父类onStart()后以Activity为准, 统一二者的SystemUiVisibility, 并取回按键输入焦点
        initParams(dialogWindow);
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

    private void initWindow() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = Objects.requireNonNull(getDialog().getWindow());
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initParams(Window window) {
        getDialog().setCancelable(mIsOutCancel);
        window.setLayout(mLayoutWidth != 0 ? DensityUtil.dp2px(mContext, mLayoutWidth) : WRAP_CONTENT,
                         mLayoutHeight != 0 ? DensityUtil.dp2px(mContext, mLayoutHeight) : WRAP_CONTENT);
        window.setGravity(mGravity);
        window.setDimAmount(mDimAmount);
        window.setWindowAnimations(mWindowAnimations);
    }

    public BaseDialogFragment setLayoutSize(int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        return this;
    }//设置大小

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

    protected abstract void initView(BaseViewHolder vh, BaseDialogFragment df);
}
