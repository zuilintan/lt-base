package com.lt.person_baseutil.view.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import com.lt.library.base.BaseFragment;
import com.lt.library.util.DensityUtil;
import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.databinding.FragmentTest01ChildTest01Binding;
import com.lt.person_baseutil.view.state.MainViewModel;

public class Test01ChildTest01Fragment extends BaseFragment<FragmentTest01ChildTest01Binding> {
    private MainViewModel mStateViewModel;
    private boolean mFillState;

    @Override
    protected FragmentTest01ChildTest01Binding bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentTest01ChildTest01Binding.inflate(inflater, container, false);
    }

    @Override
    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.bindData(arguments, savedInstanceState);
        mStateViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        mViewBinding.button.setText("切至全屏");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mViewBinding.button.setOnClickListener(v -> {
            mStateViewModel.requestFill(!mFillState);
            if (!mFillState) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mViewBinding.getRoot());
                constraintSet.constrainHeight(mViewBinding.textView.getId(), DensityUtil.dp2px(128));
                constraintSet.applyTo(mViewBinding.getRoot());
                mViewBinding.button.setText("切至半屏");
            } else {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mViewBinding.getRoot());
                constraintSet.constrainHeight(mViewBinding.textView.getId(), DensityUtil.dp2px(32));
                constraintSet.applyTo(mViewBinding.getRoot());
                mViewBinding.button.setText("切至全屏");
            }
            mFillState = !mFillState;
        });
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d();
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        LogUtil.d();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d("isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d();
    }
}
