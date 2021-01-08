package com.lt.person_baseutil.view.adapter;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lt.library.base.recyclerview.adapter.BaseAdapter;
import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.holder.sub.EntityViewHolder;
import com.lt.library.util.DensityUtil;
import com.lt.person_baseutil.R;

import java.util.Objects;

public class PreviewWidgetAdapter extends BaseAdapter<AppWidgetProviderInfo> {
    private AppWidgetHost mWidgetHost;

    public PreviewWidgetAdapter(AppWidgetHost widgetHost) {
        mWidgetHost = widgetHost;
    }

    @Override
    protected int getEntityLayoutRes(int viewType) {
        return R.layout.item_widget_with_preview;
    }

    @Override
    protected void onBindEntityView(EntityViewHolder viewHolder, AppWidgetProviderInfo dataSource, int position, int viewType) {
        super.onBindEntityView(viewHolder, dataSource, position, viewType);
        Context context = viewHolder.itemView.getContext();
        LinearLayout rootView = viewHolder.findViewById(R.id.ll_item_root);
        if (Objects.nonNull(dataSource)) {
            Drawable iconImage = dataSource.loadIcon(getAppContext(), dataSource.icon);
            ImageView iconImageView = new ImageView(context);
            iconImageView.setImageDrawable(iconImage);
            LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(100),
                                                                                       ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.addView(iconImageView, iconLayoutParams);

            Drawable previewImage = dataSource.loadPreviewImage(getAppContext(), dataSource.previewImage);
            ImageView previewImageView = new ImageView(context);
            previewImageView.setImageDrawable(previewImage);
            LinearLayout.LayoutParams previewLayoutParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(200),
                                                                                          ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.addView(previewImageView, previewLayoutParams);
        }
    }

    @Override
    protected void onRecycledView(BaseViewHolder viewHolder, int viewType) {
        super.onRecycledView(viewHolder, viewType);
        LinearLayout rootView = viewHolder.findViewById(R.id.ll_item_root);
        rootView.removeAllViews();
    }
}
