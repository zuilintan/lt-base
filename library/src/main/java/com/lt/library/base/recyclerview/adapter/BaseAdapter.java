package com.lt.library.base.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.holder.sub.EntityViewHolder;
import com.lt.library.base.recyclerview.holder.sub.ExtrasViewHolder;
import com.lt.library.base.recyclerview.holder.sub.FooterViewHolder;
import com.lt.library.base.recyclerview.holder.sub.HeaderViewHolder;
import com.lt.library.base.recyclerview.holder.sub.StatusViewHolder;
import com.lt.library.base.recyclerview.listener.OnEntityItemClickListener;
import com.lt.library.base.recyclerview.listener.OnEntityItemLongClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasItemClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasItemLongClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterItemClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterItemLongClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderItemClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderItemLongClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusItemClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusItemLongClickListener;
import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: LinTan
 * @日期: 2018/12/12 12:10
 * @版本: 1.0
 * @描述: //BaseAdapter, 注意引入依赖
 * 源址: https://blog.csdn.net/a_zhon/article/details/66971369
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public abstract class BaseAdapter<DS> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int ITEM_TYPE_HEADER = 1001;
    private static final int ITEM_TYPE_STATUS = 1002;
    private static final int ITEM_TYPE_ENTITY = 1003;
    private static final int ITEM_TYPE_EXTRAS = 1004;
    private static final int ITEM_TYPE_FOOTER = 1005;
    private final List<DS> mEntityList;
    protected RecyclerView mRecyclerView;
    private OnHeaderItemClickListener mOnHeaderItemClickListener;
    private OnHeaderItemLongClickListener mOnHeaderItemLongClickListener;
    private OnStatusItemClickListener mOnStatusItemClickListener;
    private OnStatusItemLongClickListener mOnStatusItemLongClickListener;
    private OnEntityItemClickListener mOnEntityItemClickListener;
    private OnEntityItemLongClickListener mOnEntityItemLongClickListener;
    private OnExtrasItemClickListener mOnExtrasItemClickListener;
    private OnExtrasItemLongClickListener mOnExtrasItemLongClickListener;
    private OnFooterItemClickListener mOnFooterItemClickListener;
    private OnFooterItemLongClickListener mOnFooterItemLongClickListener;
    private int mHeaderCount, mStatusCount, mExtrasCount, mFooterCount = 0;
    private int mHeaderViewId, mStatusViewId, mExtrasViewId, mFooterViewId = -1;

    public BaseAdapter() {
        this(null);
    }//需要后续主动调用notifyEntityRefAll(List<DS> dataSourceList)添加数据源

    public BaseAdapter(List<DS> entityList) {
        mEntityList = new ArrayList<>();
        if (entityList != null) {
            mEntityList.addAll(entityList);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        BaseViewHolder viewHolder;
        if (viewType == RecyclerView.INVALID_TYPE) {
            throw new IllegalArgumentException("viewType = " + viewType + ", invalid");
        }
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mHeaderViewId, viewGroup, false);
                viewHolder = new HeaderViewHolder(view, mOnHeaderItemClickListener, mOnHeaderItemLongClickListener);
                break;
            case ITEM_TYPE_STATUS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mStatusViewId, viewGroup, false);
                viewHolder = new StatusViewHolder(view, mOnStatusItemClickListener, mOnStatusItemLongClickListener);
                break;
            case ITEM_TYPE_EXTRAS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mExtrasViewId, viewGroup, false);
                viewHolder = new ExtrasViewHolder(view, mOnExtrasItemClickListener, mOnExtrasItemLongClickListener);
                break;
            case ITEM_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mFooterViewId, viewGroup, false);
                viewHolder = new FooterViewHolder(view, mOnFooterItemClickListener, mOnFooterItemLongClickListener);
                break;
            case ITEM_TYPE_ENTITY:
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(getEntityLayoutRes(viewType), viewGroup, false);
                viewHolder = new EntityViewHolder(view, mOnEntityItemClickListener, mOnEntityItemLongClickListener);
                break;
        }
        return viewHolder;
    }//创建视图管理器

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            onBindHeaderView((HeaderViewHolder) viewHolder);
        } else if (viewHolder instanceof StatusViewHolder) {
            onBindStatusView((StatusViewHolder) viewHolder);
        } else if (viewHolder instanceof EntityViewHolder) {
            int fixEntityPosition = getFixEntityPosition(position);
            viewHolder.itemView.setTag(fixEntityPosition);
            onBindEntityView((EntityViewHolder) viewHolder, getEntity(fixEntityPosition), fixEntityPosition, viewHolder.getItemViewType());
        } else if (viewHolder instanceof ExtrasViewHolder) {
            onBindExtrasView((ExtrasViewHolder) viewHolder);
        } else if (viewHolder instanceof FooterViewHolder) {
            onBindFooterView((FooterViewHolder) viewHolder);
        }
    }//绑定数据到视图

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads);
        }//更新整个item
        else {
            if (viewHolder instanceof EntityViewHolder) {
                int fixEntityPosition = getFixEntityPosition(position);
                onBindEntityView((EntityViewHolder) viewHolder, getEntity(fixEntityPosition), fixEntityPosition, viewHolder.getItemViewType(), payloads);
            }
        }//更新item中特定View
    }//绑定数据到视图(局部刷新)

    @Override
    public int getItemViewType(int position) {
        int result;
        if (isHeader(position)) {
            result = ITEM_TYPE_HEADER;
        } else if (isStatus(position)) {
            result = ITEM_TYPE_STATUS;
        } else if (isEntity(position)) {
            result = getEntityViewType(getFixEntityPosition(position));
        } else if (isExtras(position)) {
            result = ITEM_TYPE_EXTRAS;
        } else if (isFooter(position)) {
            result = ITEM_TYPE_FOOTER;
        } else {
            result = RecyclerView.INVALID_TYPE;
        }
        return result;
    }//获取ItemView的类型

    @Override
    public long getItemId(int position) {
        long result;
        if (hasStableIds() && isEntity(position)) {
            result = getEntity(getFixEntityPosition(position)).hashCode();
        } else {
            result = RecyclerView.NO_ID;
        }
        return result;
    }//为每个Item绑定唯一的Id, 需搭配adapter.setHasStableIds(true), 且要在setAdapter()前调用

    @Override
    public int getItemCount() {
        int entityViewCount = getEntityListSize();
        return mHeaderCount + mStatusCount + entityViewCount + mExtrasCount + mFooterCount;
    }//获取Item的总数

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        onRecycledView(viewHolder, viewHolder.getItemViewType());
    }//Item回收时回调

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        mergeStGridLayoutManagerFullSpan(viewHolder);//当LayoutManager为StaggeredGridLayoutManager时, 让非EntityView占满所在行
    }//Item滑入屏幕时回调

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
    }//Item滑出屏幕时回调

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mergeGridLayoutManagerFullSpan(recyclerView);//当LayoutManager为GridLayoutManager时, 让非EntityView占满所在行
        mRecyclerView = recyclerView;
    }//setAdapter()后新的Adapter回调

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }//setAdapter()后旧的Adapter回调

    private boolean isHeader(int position) {
        return mHeaderCount > 0
                && position < mHeaderCount;
    }

    private boolean isStatus(int position) {
        return mStatusCount > 0
                && mHeaderCount <= position
                && position < mHeaderCount + mStatusCount;
    }

    private boolean isEntity(int position) {
        int entityViewCount = getEntityListSize();
        return entityViewCount > 0
                && mHeaderCount + mStatusCount <= position
                && position < mHeaderCount + mStatusCount + entityViewCount;
    }

    private boolean isExtras(int position) {
        int entityViewCount = getEntityListSize();
        return mExtrasCount > 0
                && mHeaderCount + mStatusCount + entityViewCount <= position
                && position < mHeaderCount + mStatusCount + entityViewCount + mExtrasCount;
    }

    private boolean isFooter(int position) {
        int entityViewCount = getEntityListSize();
        return mFooterCount > 0
                && mHeaderCount + mStatusCount + entityViewCount + mExtrasCount <= position;
    }

    private int getFixEntityPosition(int rawPosition) {
        return rawPosition - (mHeaderCount + mStatusCount);
    }

    private int getRawEntityPosition(int fixPosition) {
        return fixPosition + (mHeaderCount + mStatusCount);
    }

    private void mergeStGridLayoutManagerFullSpan(@NonNull BaseViewHolder viewHolder) {
        ViewGroup.LayoutParams viewGroupLayoutParams = viewHolder.itemView.getLayoutParams();
        if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sgLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
            if (!isEntity(viewHolder.getLayoutPosition())) {
                sgLayoutManagerLayoutParams.setFullSpan(true);
            }
        }
    }

    private void mergeGridLayoutManagerFullSpan(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager rcvLayoutManager = recyclerView.getLayoutManager();
        if (rcvLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) rcvLayoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (!isEntity(position)) {
                        spanSize = gridLayoutManager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }
    }

    private boolean clickEntity(int rawEntityPosition) {
        boolean result;
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(rawEntityPosition);
        if (viewHolder == null) {
            result = false;
        }//表示通知数据源更新后(eg: notifyDataSetChanged()), RecyclerView尚未计算完成的布局, 此时position是未知的
        else {
            LogUtil.d("programmatic click execute start");
            viewHolder.itemView.callOnClick();
            LogUtil.d("programmatic click execute end");
            result = true;
        }//布局计算完成
        return result;
    }

    protected Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    @IntRange(from = 0)
    protected int getEntityViewType(int position) {
        return ITEM_TYPE_ENTITY;
    }

    @LayoutRes
    protected abstract int getEntityLayoutRes(int viewType);

    protected void onBindHeaderView(HeaderViewHolder viewHolder) {
    }

    protected void onBindStatusView(StatusViewHolder viewHolder) {
    }

    protected void onBindEntityView(EntityViewHolder viewHolder, DS dataSource, int position, int viewType) {
    }

    protected void onBindEntityView(EntityViewHolder viewHolder, DS dataSource, int position, int viewType, List<Object> payloads) {
    }

    protected void onBindExtrasView(ExtrasViewHolder viewHolder) {
    }

    protected void onBindFooterView(FooterViewHolder viewHolder) {
    }

    protected void onRecycledView(BaseViewHolder viewHolder, int viewType) {
    }

    public void setHeader(@LayoutRes int layoutResId) {
        delHeader();
        addHeader(layoutResId);
    }//设置HeaderView

    public void delHeader() {
        if (mHeaderViewId == -1 || mHeaderCount == 0) {
            LogUtil.w("headerView not added, headerViewId = " + mHeaderViewId + ", headerViewCount = " + mHeaderCount);
            return;
        }
        mHeaderViewId = -1;
        mHeaderCount = 0;
        notifyItemRemoved(0);
    }//移除HeaderView

    public void addHeader(@LayoutRes int layoutResId) {
        if (mHeaderViewId != -1 || mHeaderCount != 0) {
            LogUtil.w("headerView not deleted, headerViewId = " + mHeaderViewId + ", headerViewCount = " + mHeaderCount);
            return;
        }
        mHeaderViewId = layoutResId;
        mHeaderCount = 1;
        notifyItemInserted(0);
    }//添加HeaderView

    public void setStatus(@LayoutRes int layoutResId) {
        delStatus();
        addStatus(layoutResId);
    }//设置StatusView

    public void delStatus() {
        if (mStatusViewId == -1 || mStatusCount == 0) {
            LogUtil.w("statusView not added, statusViewId = " + mStatusViewId + ", statusViewCount = " + mStatusCount);
            return;
        }
        mStatusViewId = -1;
        mStatusCount = 0;
        notifyItemRemoved(mHeaderCount);
    }//移除StatusView

    public void addStatus(@LayoutRes int layoutResId) {
        if (mStatusViewId != -1 || mStatusCount != 0) {
            LogUtil.w("statusView not deleted, statusViewId = " + mStatusViewId + ", statusViewCount = " + mStatusCount);
            return;
        }
        mStatusViewId = layoutResId;
        mStatusCount = 1;
        notifyItemInserted(mHeaderCount);
    }//添加StatusView

    public void setExtras(@LayoutRes int layoutResId) {
        delExtras();
        addExtras(layoutResId);
    }//设置ExtrasView

    public void delExtras() {
        if (mExtrasViewId == -1 || mExtrasCount == 0) {
            LogUtil.w("extrasView not added, extrasViewId = " + mExtrasViewId + ", extrasViewCount = " + mExtrasCount);
            return;
        }
        mExtrasViewId = -1;
        mExtrasCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderCount + mStatusCount + entityViewCount);
    }//移除ExtrasView

    public void addExtras(@LayoutRes int layoutResId) {
        if (mExtrasViewId != -1 || mExtrasCount != 0) {
            LogUtil.w("extrasView not deleted, extrasViewId = " + mExtrasViewId + ", extrasViewCount = " + mExtrasCount);
            return;
        }
        mExtrasViewId = layoutResId;
        mExtrasCount = 1;
        int entityViewCount = getEntityListSize();
        notifyItemInserted(mHeaderCount + mStatusCount + entityViewCount);
    }//添加ExtrasView

    public void setFooter(@LayoutRes int layoutResId) {
        delFooter();
        addFooter(layoutResId);
    }//设置FooterView

    public void delFooter() {
        if (mFooterViewId == -1 || mFooterCount == 0) {
            LogUtil.w("footerView not added, footerViewId = " + mFooterViewId + ", footerViewCount = " + mFooterCount);
            return;
        }
        mFooterViewId = -1;
        mFooterCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderCount + mStatusCount + entityViewCount + mExtrasCount + mFooterCount);
    }//移除FooterView

    public void addFooter(@LayoutRes int layoutResId) {
        if (mFooterViewId != -1 || mFooterCount != 0) {
            LogUtil.w("footerView not deleted, footerViewId = " + mFooterViewId + ", footerViewCount = " + mFooterCount);
            return;
        }
        mFooterViewId = layoutResId;
        mFooterCount = 1;
        int entityViewCount = getEntityListSize();
        notifyItemInserted(mHeaderCount + mStatusCount + entityViewCount + mExtrasCount + mFooterCount);
    }//添加FooterView

    public void notifyEntitySetChanged(List<DS> dataSourceList) {
        if (!mEntityList.isEmpty()) {
            mEntityList.clear();
        }
        mEntityList.addAll(dataSourceList);
        notifyDataSetChanged();
    }//刷新数据源集合, 并更新item, 性能较低, 且无动画

    public void notifyEntityRefAll(List<DS> dataSourceList) {
        notifyEntityDelAll();
        notifyEntityAddAll(dataSourceList);
    }//刷新数据源集合, 并更新item

    public void notifyEntityDelAll() {
        if (mEntityList.isEmpty()) {
            LogUtil.w("EntityView not added");
            return;
        }
        int entityViewCount = getEntityListSize();
        mEntityList.clear();
        notifyItemRangeRemoved(mHeaderCount + mStatusCount, entityViewCount);
        notifyItemRangeChanged(mHeaderCount + mStatusCount, entityViewCount);
    }//清空数据源集合, 并更新item

    public void notifyEntityAddAll(List<DS> dataSourceList) {
        int entityViewCount = getEntityListSize();
        mEntityList.addAll(dataSourceList);
        notifyItemRangeInserted(mHeaderCount + mStatusCount + entityViewCount, dataSourceList.size());
        notifyItemRangeChanged(mHeaderCount + mStatusCount + entityViewCount, dataSourceList.size());
    }//添加数据源集合, 并更新item

    public void notifyEntityRef(DS dataSource, int position) {
        mEntityList.set(position, dataSource);
        notifyItemChanged(mHeaderCount + mStatusCount + position);
    }//刷新数据源, 并更新item

    public void notifyEntityRef(DS dataSource, int position, Object payload) {
        mEntityList.set(position, dataSource);
        notifyItemChanged(mHeaderCount + mStatusCount + position, payload);
    }//刷新数据源, 并更新item中的View(局部刷新)

    public void notifyEntityDel(int position) {
        int entityViewCount = getEntityListSize();
        mEntityList.remove(position);
        notifyItemRemoved(mHeaderCount + mStatusCount + position);
        notifyItemRangeChanged(mHeaderCount + mStatusCount + position, entityViewCount - position);
    }//清空数据源, 并更新item

    public void notifyEntityAdd(DS dataSource, int position) {
        int entityViewCount = getEntityListSize();
        mEntityList.add(position, dataSource);
        notifyItemInserted(mHeaderCount + mStatusCount + position);
        notifyItemRangeChanged(mHeaderCount + mStatusCount + position, entityViewCount - position);
    }//添加数据源, 并更新item

    public void notifyEntityClick(int position) {
        int rawEntityPosition = getRawEntityPosition(position);
        if (isEntity(rawEntityPosition)) {
            if (!clickEntity(rawEntityPosition)) {
                LogUtil.d("programmatic click no ready, the layout has not been calculated yet");
                mRecyclerView.scrollToPosition(rawEntityPosition);
                mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        LogUtil.d("programmatic click is ready");
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);//移除监听, 避免反复回调重复执行clickEntity()
                        clickEntity(rawEntityPosition);
                    }//当布局可视时回调
                });//注册RecyclerView视图的可视状态变化监听器
            }
        } else {
            LogUtil.w("position = " + position + ", out of bounds");
        }
    }//编程式点击Item(无Beep音), 需要在setAdapter()及添加数据源后(eg: notifyEntityRefAll())调用

    public DS getEntity(int position) {
        return mEntityList.get(position);
    }//获取数据源

    public List<DS> getEntityList() {
        return mEntityList;
    }//获取数据源集合

    public int getEntityListSize() {
        return mEntityList.size();
    }//获取数据源集合大小

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener onHeaderItemClickListener) {
        mOnHeaderItemClickListener = onHeaderItemClickListener;
    }

    public void setOnHeaderItemLongClickListener(OnHeaderItemLongClickListener onHeaderItemLongClickListener) {
        mOnHeaderItemLongClickListener = onHeaderItemLongClickListener;
    }

    public void setOnStatusItemClickListener(OnStatusItemClickListener onStatusItemClickListener) {
        mOnStatusItemClickListener = onStatusItemClickListener;
    }

    public void setOnStatusItemLongClickListener(OnStatusItemLongClickListener onStatusItemLongClickListener) {
        mOnStatusItemLongClickListener = onStatusItemLongClickListener;
    }

    public void setOnEntityItemClickListener(OnEntityItemClickListener onEntityItemClickListener) {
        mOnEntityItemClickListener = onEntityItemClickListener;
    }

    public void setOnEntityItemLongClickListener(OnEntityItemLongClickListener onEntityItemLongClickListener) {
        mOnEntityItemLongClickListener = onEntityItemLongClickListener;
    }

    public void setOnExtrasItemClickListener(OnExtrasItemClickListener onExtrasItemClickListener) {
        mOnExtrasItemClickListener = onExtrasItemClickListener;
    }

    public void setOnExtrasItemLongClickListener(OnExtrasItemLongClickListener onExtrasItemLongClickListener) {
        mOnExtrasItemLongClickListener = onExtrasItemLongClickListener;
    }

    public void setOnFooterItemClickListener(OnFooterItemClickListener onFooterItemClickListener) {
        mOnFooterItemClickListener = onFooterItemClickListener;
    }

    public void setOnFooterItemLongClickListener(OnFooterItemLongClickListener onFooterItemLongClickListener) {
        mOnFooterItemLongClickListener = onFooterItemLongClickListener;
    }
}
