package com.lt.person_baseutil.view.adapter;

import android.widget.SeekBar;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.lt.library.base.recyclerview.adapter.BaseAdapter;
import com.lt.library.base.recyclerview.viewholder.EntityViewHolder;
import com.lt.person_baseutil.R;
import com.lt.person_baseutil.model.entity.TestBean;

import java.util.List;

public class TestAdapter extends BaseAdapter<TestBean> {

    public TestAdapter() {
    }

    public TestAdapter(List<TestBean> entityList) {
        super(entityList);
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
                  .setText(R.id.tv_item_title, dataSource.getTitle());
        switch (viewType) {
            case TestBean.ITEM_TYPE_CONTENT:
                TextView textView = viewHolder.findViewById(R.id.tv_item_content);
                textView.setText(dataSource.getItemValue());
                break;
            case TestBean.ITEM_TYPE_SWITCH:
                viewHolder.setImageDrawable(R.id.iv_item_ico, R.drawable.ic_fire);
                SwitchButton switchButton = viewHolder.findViewById(R.id.sBtn_item_switch);
                switchButton.setChecked(Boolean.parseBoolean(dataSource.getItemValue()));
                break;
            case TestBean.ITEM_TYPE_SLIDE:
                viewHolder.setImageDrawable(R.id.iv_item_ico, R.drawable.ic_toy);
                SeekBar seekBar = viewHolder.findViewById(R.id.sb_item_slide);
                seekBar.setProgress(Integer.parseInt(dataSource.getItemValue()));
                break;
        }
    }

    @Override
    protected int getEntityViewType(int position) {
        return getEntity(position).getItemType();
    }
}
