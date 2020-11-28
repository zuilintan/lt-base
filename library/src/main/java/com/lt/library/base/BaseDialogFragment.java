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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hunter.library.debug.HunterDebugImpl;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;
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

public abstract class BaseDialogFragment extends DialogFragment {
    protected FragmentActivity mActivity;
    private Integer mLayoutWidth;
    private Integer mLayoutHeight;
    private Integer mOffsetX;
    private Integer mOffsetY;
    private Integer mGravity;
    private Integer mWindowAnimation;
    private Float mDimAmount;
    private Boolean mIsOutCancel;
    private boolean mIsKeepSystemUiState;

    @HunterDebugImpl
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @HunterDebugImpl
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @HunterDebugImpl
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }//Plan A, 蠢拒, 一般用于创建替代传统的Dialog对话框的场景, UI简单, 功能单一

    @HunterDebugImpl
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(bindView(), container, false);
    }//Plan B, 推荐, 一般用于创建复杂内容弹窗, 全屏展示效果, 或有网络请求等异步操作的场景, UI复杂, 功能繁多

    @HunterDebugImpl
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData(getArguments(), savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Objects.nonNull(mIsOutCancel)) {
            getDialog().setCanceledOnTouchOutside(mIsOutCancel);
        }
        initView(new BaseViewHolder(view));
        initData();
    }

    @HunterDebugImpl
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @HunterDebugImpl
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
            initParam(dW);
        } else {
            LogUtil.w("activity getWindow = " + aW + ", dialog getWindow = " + dW);
            super.onStart();
        }
    }

    @HunterDebugImpl
    @Override
    public void onResume() {
        super.onResume();
    }

    @HunterDebugImpl
    @Override
    public void onPause() {
        super.onPause();
    }

    @HunterDebugImpl
    @Override
    public void onStop() {
        super.onStop();
    }

    @HunterDebugImpl
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @HunterDebugImpl
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeData();
        freeView();
    }

    @HunterDebugImpl
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @HunterDebugImpl
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @HunterDebugImpl
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @HunterDebugImpl
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @HunterDebugImpl
    @Override
    public void show(FragmentManager manager, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (Objects.isNull(fragment)) {
            super.show(manager, tag);
        }//规避并发点击时, IllegalStateException: Fragment already added
        else {
            LogUtil.w("dialogFragment added, tag = " + tag);
        }
    }

    @HunterDebugImpl
    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        } else {
            LogUtil.w("dialogFragment not added");
        }
    }

    private void initParam(@NonNull Window dW) {
        dW.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Objects.nonNull(mLayoutWidth) && Objects.nonNull(mLayoutHeight)) {
            dW.setLayout(DensityUtil.dp2px(mLayoutWidth),
                         DensityUtil.dp2px(mLayoutHeight));
        } else {
            dW.setLayout((int) (ScreenUtil.getScreenWidth() * 0.75),
                         (int) (ScreenUtil.getScreenHeight() * 0.50));
        }
        if (Objects.nonNull(mOffsetX)) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.x = DensityUtil.dp2px(mOffsetX);
            dW.setAttributes(attrs);
        }
        if (Objects.nonNull(mOffsetY)) {
            final WindowManager.LayoutParams attrs = dW.getAttributes();
            attrs.y = DensityUtil.dp2px(mOffsetY);
            dW.setAttributes(attrs);
        }
        if (Objects.nonNull(mGravity)) {
            dW.setGravity(mGravity);
        }
        if (Objects.nonNull(mWindowAnimation)) {
            dW.setWindowAnimations(mWindowAnimation);
        }
        if (Objects.nonNull(mDimAmount)) {
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

    public BaseDialogFragment setLayout(@Dimension(unit = Dimension.DP) int width, int height) {
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

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplicationContext();
    }

    protected abstract int bindView();//绑定视图

    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    }//绑定数据(eg: Bundle, SaveInstanceState, SharedPreferences)

    protected void initView(BaseViewHolder viewHolder) {
    }//初始化视图

    protected void initData() {
    }//初始化数据

    protected void saveState(@NonNull Bundle outState) {
    }//存储临时数据(eg: SaveInstanceState)

    protected void freeData() {
    }//释放数据

    protected void freeView() {
    }//释放视图
}
