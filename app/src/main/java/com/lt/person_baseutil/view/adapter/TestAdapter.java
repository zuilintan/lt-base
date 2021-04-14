package com.lt.person_baseutil.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.lt.library.base.recyclerview.adapter.BaseAdapter;
import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.holder.sub.EntityViewHolder;
import com.lt.library.util.ToastUtil;
import com.lt.person_baseutil.R;
import com.lt.person_baseutil.model.entity.TestBean;

import java.util.List;
import java.util.Objects;

public class TestAdapter extends BaseAdapter<TestBean> {
    private Toast mToast;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = (buttonView, isChecked) -> {
        if (mToast != null) {
            mToast.cancel();
        }
        new ToastUtil.Builder()
                .setText("tag = " + buttonView.getTag() + ", isChecked = " + isChecked + ", isPressed = " + buttonView.isPressed())
                .setLayout(R.layout.toast_test, R.id.tv_test)
                .build()
                .show();
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mToast = new ToastUtil.Builder()
                    .setText("tag = " + seekBar.getTag() + ", progress = " + progress + ", isFromUser = " + fromUser)
                    .build()
                    .showNow(mToast);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public TestAdapter() {
    }

    public TestAdapter(List<TestBean> entityList) {
        super(entityList);
    }

    @Override
    protected int getEntityViewType(int position) {
        return getEntity(position).getItemType();
    }

    @Override
    protected int getEntityLayoutRes(int viewType) {
        int layoutRes;
        switch (viewType) {
            case TestBean.ITEM_TYPE_CONTENT:
                layoutRes = R.layout.item_test_content;
                break;
            case TestBean.ITEM_TYPE_SWITCH:
                layoutRes = R.layout.item_test_switch;
                break;
            case TestBean.ITEM_TYPE_SLIDE:
                layoutRes = R.layout.item_test_slide;
                break;
            default:
                throw new IllegalArgumentException("unexpected viewType = " + viewType);
        }
        return layoutRes;
    }

    @Override
    protected void onBindEntityView(EntityViewHolder viewHolder, TestBean dataSource, int position, int viewType) {
        super.onBindEntityView(viewHolder, dataSource, position, viewType);
        viewHolder.setImageDrawable(R.id.iv_item_ico, Integer.parseInt(dataSource.getIco()))
                  .setText(R.id.tv_item_title, dataSource.getTitle())
                  .setBackground(R.id.csl_item_root, dataSource.isSelect() ? android.R.color.holo_purple : android.R.color.white);
        switch (viewType) {
            case TestBean.ITEM_TYPE_CONTENT:
                TextView textView = viewHolder.findViewById(R.id.tv_item_content);
                textView.setText(dataSource.getItemValue());
                break;
            case TestBean.ITEM_TYPE_SWITCH:
                viewHolder.setImageDrawable(R.id.iv_item_ico, R.drawable.ic_fire);
                SwitchButton switchButton = viewHolder.findViewById(R.id.sBtn_item_switch);
                switchButton.setChecked(Boolean.parseBoolean(dataSource.getItemValue()));
                if ((position & 1) == 1) {
                    switchButton.setTag("sBtn_tag");
                }
                switchButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
                break;
            case TestBean.ITEM_TYPE_SLIDE:
                viewHolder.setImageDrawable(R.id.iv_item_ico, R.drawable.ic_toy);
                SeekBar seekBar = viewHolder.findViewById(R.id.sb_item_slide);
                seekBar.setProgress(Integer.parseInt(dataSource.getItemValue()));
                if ((position & 1) == 1) {
                    seekBar.setTag("sb_tag");
                }
                seekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
                break;
        }
    }

    @Override
    protected void onRecycledView(BaseViewHolder viewHolder, int viewType) {
        super.onRecycledView(viewHolder, viewType);
        switch (viewType) {
            case TestBean.ITEM_TYPE_SWITCH:
                SwitchButton switchButton = viewHolder.findViewById(R.id.sBtn_item_switch);
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setTag(null);
                break;
            case TestBean.ITEM_TYPE_SLIDE:
                SeekBar seekBar = viewHolder.findViewById(R.id.sb_item_slide);
                seekBar.setOnSeekBarChangeListener(null);
                seekBar.setTag(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mOnCheckedChangeListener = null;
        mOnSeekBarChangeListener = null;
    }

    public void notifyDataPositionSelected(int newSelectedPosition) {
        TestBean newListBean = getEntity(newSelectedPosition);
        if (newListBean.isSelect()) {
            return;
        }
        for (int i = 0; i < getEntityList().size(); i++) {
            TestBean listBean = getEntity(i);
            if (Objects.equals(listBean, newListBean)) {
                continue;
            }
            if (listBean.isSelect()) {
                listBean.setSelect(false);
                notifyEntityRef(listBean, i);
            }
        }
        newListBean.setSelect(true);
        notifyEntityRef(newListBean, newSelectedPosition);
    }
}
