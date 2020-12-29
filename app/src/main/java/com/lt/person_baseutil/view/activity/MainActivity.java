package com.lt.person_baseutil.view.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kyleduo.switchbutton.SwitchButton;
import com.lt.library.base.BaseActivity;
import com.lt.library.base.recyclerview.listener.OnEntityClickListener;
import com.lt.library.util.CastUtil;
import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.R;
import com.lt.person_baseutil.databinding.ActivityMainBinding;
import com.lt.person_baseutil.model.repo.DataRepo;
import com.lt.person_baseutil.view.adapter.TestAdapter;

import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements
        OnEntityClickListener {
    private TestAdapter mTestAdapter;

    @Override
    protected ActivityMainBinding bindView() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        super.initView();
        mTestAdapter = new TestAdapter(DataRepo.getInstance().getTestData());
        mViewBinding.rcvMainTest.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rcvMainTest.setAdapter(mTestAdapter);
        mViewBinding.rcvMainTest.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTestAdapter.setOnEntityClickListener(this);
    }

    @Override
    protected void freeEvent() {
        super.freeEvent();
        mTestAdapter.setOnEntityClickListener(null);
    }

    @Override
    protected void freeView() {
        super.freeView();
        mTestAdapter = null;
        mViewBinding.rcvMainTest.setAdapter(null);
    }

    @Override
    public void onEntityClick(View view, int position) {
        if (view instanceof ConstraintLayout) {
            ConstraintLayout constraintLayout = (ConstraintLayout) view;
            SwitchButton sBtn = (SwitchButton) constraintLayout.getViewById(R.id.sBtn_item_switch);
            if (Objects.nonNull(sBtn)) {
                sBtn.toggle();
            }
        }
        if ((position & 1) == 1) {
            test(1.0);
        } else {
            test("GG");
        }
    }

    private void test(Object object) {
        float f = 1.0F;
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, Integer.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, Float.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, Double.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, int.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, float.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, double.class));
        LogUtil.w("isInstanceOf = " + CastUtil.obj2Obj(object, String.class));
    }
}
