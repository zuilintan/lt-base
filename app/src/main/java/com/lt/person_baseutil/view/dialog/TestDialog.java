package com.lt.person_baseutil.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lt.library.base.dialogfragment.BaseDialogFragment;
import com.lt.person_baseutil.databinding.DlgTestBinding;

import java.util.Objects;

public class TestDialog extends BaseDialogFragment<DlgTestBinding> {
    private String mMsg;

    public static TestDialog newInstance(String msg) {
        Bundle args = new Bundle();
        args.putString("arg1", msg);
        TestDialog fragment = new TestDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DlgTestBinding bindView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DlgTestBinding.inflate(inflater, container, false);
    }

    @Override
    protected void bindData(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.bindData(arguments, savedInstanceState);
        if (Objects.nonNull(arguments)) {
            mMsg = arguments.getString("arg1");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mViewBinding.tvTest.setText(mMsg);
    }
}
