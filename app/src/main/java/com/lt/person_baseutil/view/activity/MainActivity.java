package com.lt.person_baseutil.view.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.kyleduo.switchbutton.SwitchButton;
import com.lt.library.base.BaseActivity;
import com.lt.library.base.recyclerview.listener.OnEntityItemClickListener;
import com.lt.library.util.CastUtil;
import com.lt.library.util.LogUtil;
import com.lt.library.util.ToastUtil;
import com.lt.person_baseutil.R;
import com.lt.person_baseutil.databinding.ActivityMainBinding;
import com.lt.person_baseutil.model.repo.DataRepo;
import com.lt.person_baseutil.view.adapter.TestAdapter;

import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements
        OnEntityItemClickListener {
    private static final int MY_REQUEST_WIDGET = 1;
    private static final int MY_CREATE_WIDGET = 2;
    private AppWidgetHost mWidgetHost;
    private AppWidgetManager mWidgetManager;
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
        mViewBinding.rcvMainTest.getItemAnimator().setChangeDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        createWeight();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTestAdapter.setOnEntityItemClickListener(this);
    }

    @Override
    protected void freeEvent() {
        super.freeEvent();
        mTestAdapter.setOnEntityItemClickListener(null);
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
        mTestAdapter.notifyDataPositionSelected(position);
        showWidget();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case MY_REQUEST_WIDGET:
                LogUtil.i("data = " + data);
                int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                LogUtil.i("widgetId = " + widgetId);
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    onActivityResult(MY_CREATE_WIDGET, RESULT_OK, data);
                }
                break;

            case MY_CREATE_WIDGET:
                addWidget(data);
                break;

            default:
                break;
        }
    }

    private void addWidget(Intent data) {
        int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        if ((widgetId == -1)) {
            ToastUtil.show("添加有误");
            return;
        }
        AppWidgetProviderInfo widgetInfo = mWidgetManager.getAppWidgetInfo(widgetId);
        AppWidgetHostView widgetHostView = mWidgetHost.createView(getAppContext(), widgetId, widgetInfo);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widgetInfo.minResizeWidth,
                                                                               widgetInfo.minResizeHeight);
        mViewBinding.llMainTest.addView(widgetHostView, layoutParams);
    }

    private void showWidget() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        int widgetId = mWidgetHost.allocateAppWidgetId();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        startActivityForResult(intent, MY_REQUEST_WIDGET);
    }

    private void createWeight() {
        mWidgetHost = new AppWidgetHost(getAppContext(), 1024);
        mWidgetHost.startListening();
        mWidgetManager = AppWidgetManager.getInstance(getAppContext());
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
