package com.lt.person_baseutil.view.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lt.library.base.BaseActivity;
import com.lt.person_baseutil.databinding.ActivityLauncherBinding;
import com.lt.person_baseutil.model.repo.DataRepo;
import com.lt.person_baseutil.view.adapter.AddedWidgetAdapter;
import com.lt.person_baseutil.view.adapter.PreviewWidgetAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LauncherActivity extends BaseActivity<ActivityLauncherBinding> {
    private static final int HOST_ID = 1024;
    private AppWidgetHost mWidgetHost;
    private AppWidgetManager mWidgetManager;
    private AddedWidgetAdapter mAddedWidgetAdapter;
    private PreviewWidgetAdapter mPreviewWidgetAdapter;

    @Override
    protected ActivityLauncherBinding bindView() {
        return ActivityLauncherBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        super.initView();
        mWidgetHost = new AppWidgetHost(getAppContext(), HOST_ID);
        mWidgetHost.startListening();
        ArrayList<AppWidgetProviderInfo> objects = new ArrayList<>();
        objects.add(null);
        objects.add(null);
        objects.add(null);
        objects.add(null);
        mPreviewWidgetAdapter = new PreviewWidgetAdapter(mWidgetHost);
        mAddedWidgetAdapter = new AddedWidgetAdapter(mWidgetHost, objects, mPreviewWidgetAdapter);
        mViewBinding.rcvLauncherAddedWidget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mViewBinding.rcvLauncherPreviewWidget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mViewBinding.rcvLauncherAddedWidget.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mViewBinding.rcvLauncherPreviewWidget.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mViewBinding.rcvLauncherAddedWidget.setAdapter(mAddedWidgetAdapter);
        mViewBinding.rcvLauncherPreviewWidget.setAdapter(mPreviewWidgetAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        List<AppWidgetProviderInfo> previewAppWidgetList = DataRepo.getInstance().getPreviewAppWidgetList();
        mPreviewWidgetAdapter.notifyEntityRefAll(previewAppWidgetList);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mPreviewWidgetAdapter.setOnEntityItemLongClickListener((view, position) -> {
            AppWidgetProviderInfo entity = mPreviewWidgetAdapter.getEntity(position);
            HashMap<Integer, AppWidgetProviderInfo> hashMap = new HashMap<>();
            hashMap.put(position, entity);
            view.startDragAndDrop(null, new View.DragShadowBuilder(view), hashMap, 0);
            return true;
        });
    }
}
