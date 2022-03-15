package com.lt.library.base.dialogfragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Dimension;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.lt.library.base.dialogfragment.listenter.OnNegativeButtonClickListener;
import com.lt.library.base.dialogfragment.listenter.OnNeutralButtonClickListener;
import com.lt.library.base.dialogfragment.listenter.OnPositiveButtonClickListener;
import com.lt.library.util.DensityUtil;
import com.lt.library.util.LogUtil;
import com.lt.library.util.ScreenUtil;
import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2019/5/15 10:23
 * @版本: 1.0
 * @描述: BaseDialogFragment
 * 1.0: Initial Commit
 * <p>
 * buildFeatures {
 * viewBinding = true
 * }
 */

public abstract class BaseDialogFragment<V extends ViewBinding> extends DialogFragment {
    protected V mViewBinding;
    protected FragmentActivity mActivity;
    protected OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    protected OnNegativeButtonClickListener mOnNegativeButtonClickListener;
    protected OnNeutralButtonClickListener mOnNeutralButtonClickListener;
    private Integer mWindowType;
    private Integer mLayoutWidth;
    private Integer mLayoutHeight;
    private Integer mOffsetX;
    private Integer mOffsetY;
    private Integer mGravity;
    private Integer mWindowAnimation;
    private Float mDimAmount;
    private Boolean mIsOutCancel;
    private Boolean mIsKeepSystemUiVisibility;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Plan A, 蠢拒, 一般用于创建替代传统的Dialog对话框的场景, UI简单, 功能单一
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * Plan B; 推荐, 一般用于创建复杂内容弹窗, 全屏展示效果, 或有网络请求等异步操作的场景, UI复杂, 功能繁多
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = bindView(inflater, container);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData(getArguments(), savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mIsOutCancel != null) {
            getDialog().setCanceledOnTouchOutside(mIsOutCancel);
        }
        if (mWindowType != null) {
            getDialog().getWindow().setType(mWindowType);
        }
        initView();
        initData();
        initEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Window aW = mActivity.getWindow();
        Window dW = getDialog().getWindow();
        if (aW != null && dW != null) {
            if (mIsKeepSystemUiVisibility != null && mIsKeepSystemUiVisibility) {
                keepSystemUiState(aW, dW);
            } else {
                super.onStart();
            }
            initParam(dW);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeEvent();
        freeData();
        freeView();
        mViewBinding = null;
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
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        //规避并发点击时, IllegalStateException: Fragment already added
        if (fragment == null) {
            super.show(manager, tag);
        } else {
            LogUtil.w("dialogFragment added, tag = " + tag);
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        } else {
            LogUtil.w("dialogFragment not added");
        }
    }

    /**
     * 设置窗口类型
     *
     * @param windowType 窗口类型 (e.g. WindowManager.LayoutParams.TYPE_SYSTEM_ERROR)
     * @return this
     */
    public BaseDialogFragment<V> setWindowType(Integer windowType) {
        mWindowType = windowType;
        return this;
    }

    /**
     * 设置布局大小
     *
     * @param width  宽度
     * @param height 高度
     * @return this
     */
    public BaseDialogFragment<V> setLayout(@Dimension(unit = Dimension.DP) int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        return this;
    }

    /**
     * 设置X轴偏移量
     *
     * @param x 水平偏移量
     * @return this
     */
    public BaseDialogFragment<V> setOffsetX(@Dimension(unit = Dimension.DP) int x) {
        mOffsetX = x;
        return this;
    }

    /**
     * 设置Y轴偏移量
     *
     * @param y 垂直偏移量
     * @return this
     */
    public BaseDialogFragment<V> setOffsetY(@Dimension(unit = Dimension.DP) int y) {
        mOffsetY = y;
        return this;
    }

    /**
     * 设置显示位置
     *
     * @param gravity 引力
     * @return this
     */
    public BaseDialogFragment<V> setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    /**
     * 设置过场动画
     *
     * @param windowAnimation 窗口动画
     * @return this
     */
    public BaseDialogFragment<V> setWindowAnimation(@StyleRes int windowAnimation) {
        mWindowAnimation = windowAnimation;
        return this;
    }

    /**
     * 设置外部暗度
     *
     * @param dimAmount 外部暗度
     * @return this
     */
    public BaseDialogFragment<V> setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    /**
     * 设置外部取消
     *
     * @param isOutCancel 是否外部取消
     * @return this
     */
    public BaseDialogFragment<V> setOutCancel(boolean isOutCancel) {
        mIsOutCancel = isOutCancel;
        return this;
    }

    /**
     * 设置保持 SystemUiVisibility
     *
     * @param isKeepSystemUiVisibility 是否保持 SystemUiVisibility
     * @return this
     */
    public BaseDialogFragment<V> setKeepSystemUiVisibility(boolean isKeepSystemUiVisibility) {
        mIsKeepSystemUiVisibility = isKeepSystemUiVisibility;
        return this;
    }

    public BaseDialogFragment<V> setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener) {
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
        return this;
    }

    public BaseDialogFragment<V> setOnNegativeButtonClickListener(OnNegativeButtonClickListener onNegativeButtonClickListener) {
        mOnNegativeButtonClickListener = onNegativeButtonClickListener;
        return this;
    }

    public BaseDialogFragment<V> setOnNeutralButtonClickListener(OnNeutralButtonClickListener onNeutralButtonClickListener) {
        mOnNeutralButtonClickListener = onNeutralButtonClickListener;
        return this;
    }

    protected void callOnPositiveButtonClick(View view, Object object) {
        if (mOnPositiveButtonClickListener != null) {
            mOnPositiveButtonClickListener.onPositiveButtonClick(this, view, object);
        }
    }

    protected void callOnNegativeButtonClick(View view, Object object) {
        if (mOnNegativeButtonClickListener != null) {
            mOnNegativeButtonClickListener.onNegativeButtonClick(this, view, object);
        }
    }

    protected void callOnNeutralButtonClick(View view, Object object) {
        if (mOnNeutralButtonClickListener != null) {
            mOnNeutralButtonClickListener.onNeutralButtonClick(this, view, object);
        }
    }

    /**
     * 绑定视图 (e.g. ViewBinding)
     *
     * @return ViewBinding
     */
    protected abstract V bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 绑定数据 (e.g. Bundle, SaveInstanceState, SharedPreferences, MMKV)
     *
     * @param arguments          外部传入的数据
     * @param savedInstanceState 已存储的实例状态
     */
    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化视图
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化事件 (e.g. OnClickListener)
     */
    protected void initEvent() {
    }

    /**
     * 存储临时数据 (e.g. SaveInstanceState)
     *
     * @param outState 用以存储实例状态的 Bundle
     */
    protected void saveState(@NonNull Bundle outState) {
    }

    /**
     * 释放事件 (e.g. OnClickListener)
     */
    protected void freeEvent() {
    }

    /**
     * 释放数据
     */
    protected void freeData() {
    }

    /**
     * 释放视图
     */
    protected void freeView() {
    }

    protected Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    private void initParam(@NonNull Window dW) {
        dW.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mLayoutWidth != null && mLayoutHeight != null) {
            dW.setLayout(DensityUtil.dp2px(mLayoutWidth),
                         DensityUtil.dp2px(mLayoutHeight));
        } else {
            int screenWidth = ScreenUtil.getScreenWidth();
            int screenHeight = ScreenUtil.getScreenHeight();
            boolean b = screenHeight > screenWidth;
            dW.setLayout((int) (b ? screenWidth * 0.75 : screenWidth * 0.50),
                         (int) (b ? screenHeight * 0.25 : screenHeight * 0.50));
        }
        if (mOffsetX != null) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.x = DensityUtil.dp2px(mOffsetX);
            dW.setAttributes(attrs);
        }
        if (mOffsetY != null) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.y = DensityUtil.dp2px(mOffsetY);
            dW.setAttributes(attrs);
        }
        if (mGravity != null) {
            dW.setGravity(mGravity);
        }
        if (mWindowAnimation != null) {
            dW.setWindowAnimations(mWindowAnimation);
        }
        if (mDimAmount != null) {
            dW.setDimAmount(mDimAmount);
        }
    }

    /**
     * 焦点变更时保持SystemUi状态
     *
     * @param aW Activity 的 Window
     * @param dW Dialog 的 Window
     */
    private void keepSystemUiState(@NonNull Window aW, @NonNull Window dW) {
        int aSystemUiVisibility = aW.getDecorView().getSystemUiVisibility();
        int dSystemUiVisibility = dW.getDecorView().getSystemUiVisibility();
        boolean isDiffVisibility = aSystemUiVisibility != dSystemUiVisibility;
        //CallSuper 前, 主动移除焦点, 规避 SystemUiVisibility 变化
        if (isDiffVisibility) {
            dW.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        super.onStart();
        //CallSuper 后, 以 Activity 为准, 统一 SystemUiVisibility, 并取回焦点
        if (isDiffVisibility) {
            dW.getDecorView().setSystemUiVisibility(aSystemUiVisibility);
            dW.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }
}
