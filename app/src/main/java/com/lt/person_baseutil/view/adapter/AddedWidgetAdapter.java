package com.lt.person_baseutil.view.adapter;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lt.library.base.recyclerview.adapter.BaseAdapter;
import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.holder.sub.EntityViewHolder;
import com.lt.library.util.CastUtil;
import com.lt.library.util.LogUtil;
import com.lt.library.util.ScreenUtil;
import com.lt.person_baseutil.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddedWidgetAdapter extends BaseAdapter<AppWidgetProviderInfo> {
    private AppWidgetHost mWidgetHost;
    private PreviewWidgetAdapter mPreviewWidgetAdapter;

    public AddedWidgetAdapter(AppWidgetHost widgetHost, List<AppWidgetProviderInfo> objects, PreviewWidgetAdapter previewWidgetAdapter) {
        super(objects);
        mWidgetHost = widgetHost;
        mPreviewWidgetAdapter = previewWidgetAdapter;
    }

    @Override
    protected int getEntityLayoutRes(int viewType) {
        return R.layout.item_widget_with_added;
    }

    @Override
    protected void onBindEntityView(EntityViewHolder viewHolder, AppWidgetProviderInfo dataSource, int position, int viewType) {
        super.onBindEntityView(viewHolder, dataSource, position, viewType);
        LogUtil.w("GG");
        Context context = viewHolder.itemView.getContext();
        LinearLayout rootView = viewHolder.findViewById(R.id.ll_item_root);
        if (Objects.nonNull(dataSource)) {
            int widgetId = mWidgetHost.allocateAppWidgetId();
            AppWidgetHostView appWidgetHostView = mWidgetHost.createView(getAppContext(), widgetId, dataSource);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (ScreenUtil.getScreenWidth() * 0.5),
                                                                                   ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.addView(appWidgetHostView, layoutParams);
        } else {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.ic_add);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (ScreenUtil.getScreenWidth() * 0.5),
                                                                                   ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.addView(imageView, layoutParams);
        }
        rootView.setOnDragListener(null);
        rootView.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    rootView.setBackgroundColor(Color.GRAY);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
                case DragEvent.ACTION_DROP:
                    rootView.setBackground(null);
                    Object localState = event.getLocalState();
                    LogUtil.d("GG = localState = " + localState);
                    if (localState instanceof HashMap<?, ?>) {
                        Map<Integer, AppWidgetProviderInfo> hashMap = CastUtil.obj2HashMap(localState, Integer.class, AppWidgetProviderInfo.class);
                        Map.Entry<Integer, AppWidgetProviderInfo> entry = hashMap.entrySet().iterator().next();
                        Integer key = entry.getKey();
                        AppWidgetProviderInfo value = entry.getValue();
                        notifyEntityRef(value, position);
                    } else {
                        LogUtil.d("GG = unknown");
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    rootView.setBackground(null);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onRecycledView(BaseViewHolder viewHolder, int viewType) {
        super.onRecycledView(viewHolder, viewType);
        LinearLayout rootView = viewHolder.findViewById(R.id.ll_item_root);
        rootView.removeAllViews();
    }
}
