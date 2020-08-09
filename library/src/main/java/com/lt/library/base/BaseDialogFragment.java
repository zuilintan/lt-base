package com.lt.library.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lt.library.util.DensityUtil;
import com.lt.library.util.LogUtil;
import com.lt.library.util.ScreenUtil;
import com.lt.library.util.context.ContextUtil;

import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2019/5/15 10:23
 * @版本: 1.0
 * @描述: //BaseDialogFragment, 注意引入依赖
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public abstract class BaseDialogFragment<A extends FragmentActivity> extends DialogFragment {
    protected static final int INVALID_VALUE = -1;
    protected A mActivity;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private int mOffsetX;
    private int mOffsetY;
    private int mGravity;
    private int mWindowAnimation;
    private float mDimAmount;
    private boolean mIsOutCancel;
    private boolean mIsKeepSystemUiState;

    public BaseDialogFragment() {
        super();
        mLayoutWidth = INVALID_VALUE;
        mLayoutHeight = INVALID_VALUE;
        mOffsetX = INVALID_VALUE;
        mOffsetY = INVALID_VALUE;
        mGravity = INVALID_VALUE;
        mWindowAnimation = INVALID_VALUE;
        mDimAmount = INVALID_VALUE;
    }

    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (A) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }//Plan A, 蠢拒, 一般用于创建替代传统的Dialog对话框的场景, UI简单, 功能单一

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window dW = getDialog().getWindow();
        if (Objects.nonNull(dW)) {
            initParamByBeforeShow(dW);
        }
        View view = inflater.inflate(getLayoutResId(), container, false);
        initView(new BaseViewHolder(view), this);
        return view;
    }//Plan B, 推荐, 一般用于创建复杂内容弹窗或全屏展示效果的场景, UI复杂, 功能复杂, 或有网络请求等异步操作

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Window aW = mActivity.getWindow();
        Window dW = getDialog().getWindow();
        if (Objects.nonNull(aW) && Objects.nonNull(dW)) {
            if (mIsKeepSystemUiState) {
                keepSystemUiState(aW, dW);
            } else {
                super.onStart();
            }
            initParamByAfterShow(dW);
        } else {
            LogUtil.w("activity getWindow = " + aW + ", dialog getWindow = " + dW);
            super.onStart();
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
        mActivity = null;
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
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        if (Objects.nonNull(fragment)) {
            fragmentTransaction.remove(fragment);
        }//规避并发点击时, IllegalStateException: Fragment already added
        fragmentTransaction.commit();
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }
    }

    private void initParamByBeforeShow(@NonNull Window dW) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(mIsOutCancel);
        dW.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mWindowAnimation != INVALID_VALUE) {
            dW.setWindowAnimations(mWindowAnimation);
        }
    }

    private void initParamByAfterShow(@NonNull Window dW) {
        if (mLayoutWidth != INVALID_VALUE && mLayoutHeight != INVALID_VALUE) {
            dW.setLayout(DensityUtil.dp2px(mLayoutWidth),
                         DensityUtil.dp2px(mLayoutHeight));
        } else {
            dW.setLayout((int) (ScreenUtil.getScreenWidth() * 0.75),
                         (int) (ScreenUtil.getScreenHeight() * 0.50));
        }
        if (mOffsetX != INVALID_VALUE) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.x = DensityUtil.dp2px(mOffsetX);
            dW.setAttributes(attrs);
        }
        if (mOffsetY != INVALID_VALUE) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.y = DensityUtil.dp2px(mOffsetY);
            dW.setAttributes(attrs);
        }
        if (mGravity != INVALID_VALUE) {
            dW.setGravity(mGravity);
        }
        if (mDimAmount != INVALID_VALUE) {
            dW.setDimAmount(mDimAmount);
        }
    }

    private void keepSystemUiState(@NonNull Window aW, @NonNull Window dW) {
        int aSystemUiVisibility = aW.getDecorView().getSystemUiVisibility();
        int dSystemUiVisibility = dW.getDecorView().getSystemUiVisibility();
        boolean isDiffVisibility = aSystemUiVisibility != dSystemUiVisibility;
        if (isDiffVisibility) {
            dW.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }//super.onStart()前, 主动移除焦点, 规避SystemUiVisibility变化
        super.onStart();
        if (isDiffVisibility) {
            dW.getDecorView().setSystemUiVisibility(aSystemUiVisibility);
            dW.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }//super.onStart()后, 以Activity为准, 统一SystemUiVisibility, 并取回焦点
    }//焦点变更时保持SystemUi状态

    public BaseDialogFragment setLayoutSize(@Dimension(unit = Dimension.DP) int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        return this;
    }//设置布局大小

    public BaseDialogFragment setOffsetX(@Dimension(unit = Dimension.DP) int x) {
        mOffsetX = x;
        return this;
    }//设置X轴偏移量

    public BaseDialogFragment setOffsetY(@Dimension(unit = Dimension.DP) int y) {
        mOffsetY = y;
        return this;
    }//设置Y轴偏移量

    public BaseDialogFragment setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }//设置显示位置

    public BaseDialogFragment setWindowAnimation(@StyleRes int windowAnimation) {
        mWindowAnimation = windowAnimation;
        return this;
    }//设置过场动画

    public BaseDialogFragment setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }//设置外围暗度

    public BaseDialogFragment setOutCancel(boolean isOutCancel) {
        mIsOutCancel = isOutCancel;
        return this;
    }//设置外部取消

    public BaseDialogFragment setKeepSystemUiState(boolean isKeepSystemUiState) {
        mIsKeepSystemUiState = isKeepSystemUiState;
        return this;
    }//设置保持SystemUi状态

    public void show(FragmentManager manager) {
        this.show(manager, getClass().getSimpleName());
    }//显示Dialog

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    protected abstract int getLayoutResId();

    protected abstract void initView(BaseViewHolder viewHolder, BaseDialogFragment dialogFragment);

    protected abstract void freeView();
}
