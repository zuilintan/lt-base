package com.lt.person_baseutil.view.activity;

import android.net.NetworkCapabilities;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kyleduo.switchbutton.SwitchButton;
import com.lt.library.base.BaseActivity;
import com.lt.library.base.recyclerview.listener.OnEntityItemClickListener;
import com.lt.library.util.LogUtil;
import com.lt.library.util.net.connection.ConnectionUtil;
import com.lt.person_baseutil.R;
import com.lt.person_baseutil.databinding.ActivityMainBinding;
import com.lt.person_baseutil.model.repo.DataRepo;
import com.lt.person_baseutil.view.adapter.TestAdapter;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements
        OnEntityItemClickListener {
    private TestAdapter mTestAdapter;
    private ConnectionUtil mConnectionUtil;

    @Override
    protected ActivityMainBinding bindView() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        super.initView();
        mTestAdapter = new TestAdapter(DataRepo.getInstance()
                                               .getTestData());
        mViewBinding.rcvMainTest.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rcvMainTest.setAdapter(mTestAdapter);
        mViewBinding.rcvMainTest.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mViewBinding.rcvMainTest.getItemAnimator()
                                .setChangeDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTestAdapter.setOnEntityItemClickListener(this);
        mConnectionUtil = new ConnectionUtil.Builder()
                .setOnNetworkConnectionListener(networkConnectionStatus -> {
                    LogUtil.d("networkConnectionStatus = " + networkConnectionStatus);
                })
                .setOnCellularNetworkConnectionListener(cellularConnectionStatus -> {
                    LogUtil.d("cellularConnectionStatus = " + cellularConnectionStatus);
                })
                .setOnWifiNetworkConnectionListener(wifiConnectionStatus -> {
                    LogUtil.d("wifiConnectionStatus = " + wifiConnectionStatus);
                })
                .build();
        LogUtil.w("isActiveNetworkValidated = " + ConnectionUtil.isActiveNetworkValidated());
        LogUtil.w("isActiveNetworkNotMetered = " + ConnectionUtil.isActiveNetworkNotMetered());
        LogUtil.w("isActiveNetworkBelongTransport = " + ConnectionUtil.isActiveNetworkBelongTransport(NetworkCapabilities.TRANSPORT_VPN));
        LogUtil.w("getActiveNetworkTransport = " + ConnectionUtil.getActiveNetworkTransport());
    }

    @Override
    protected void freeEvent() {
        super.freeEvent();
        mConnectionUtil.release();
    }

    @Override
    public void onEntityClick(View view, int position) {
        mTestAdapter.notifyDataPositionSelected(position);
        if (view instanceof ConstraintLayout) {
            ConstraintLayout constraintLayout = (ConstraintLayout) view;
            SwitchButton sBtn = (SwitchButton) constraintLayout.getViewById(R.id.sBtn_item_switch);
            if (sBtn != null) {
                sBtn.toggle();
            }
        }
        if ((position & 1) == 1) {
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
