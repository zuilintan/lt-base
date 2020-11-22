package com.lt.library.base.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lt.library.base.recyclerview.listener.OnEntityClickListener;
import com.lt.library.base.recyclerview.listener.OnEntityLongClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasLongClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterLongClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderLongClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;
import com.lt.library.base.recyclerview.viewholder.EntityViewHolder;
import com.lt.library.base.recyclerview.viewholder.ExtrasViewHolder;
import com.lt.library.base.recyclerview.viewholder.FooterViewHolder;
import com.lt.library.base.recyclerview.viewholder.HeaderViewHolder;
import com.lt.library.base.recyclerview.viewholder.StatusViewHolder;
import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_STATUS = 2;
    private static final int VIEW_TYPE_ENTITY = 3;
    private static final int VIEW_TYPE_EXTRAS = 4;
    private static final int VIEW_TYPE_FOOTER = 5;
    private RecyclerView mRecyclerView;
    private OnHeaderClickListener mOnHeaderClickListener;
    private OnHeaderLongClickListener mOnHeaderLongClickListener;
    private OnStatusClickListener mOnStatusClickListener;
    private OnStatusLongClickListener mOnStatusLongClickListener;
    private OnEntityClickListener mOnEntityClickListener;
    private OnEntityLongClickListener mOnEntityLongClickListener;
    private OnExtrasClickListener mOnExtrasClickListener;
    private OnExtrasLongClickListener mOnExtrasLongClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private OnFooterLongClickListener mOnFooterLongClickListener;
    private int mHeaderCount = 0;
    private int mStatusCount = 0;
    private int mExtrasCount = 0;
    private int mFooterCount = 0;
    private final List<DS> mEntityList;
    private int mHeaderId = -1;
    private int mStatusId = -1;
    private final int mEntityId = getLayoutRes();
    private int mExtrasId = -1;
    private int mFooterId = -1;

    public BaseAdapter() {
        this(null);
    }//需要后续主动调用notifyEntityRefAll(List<DS> dataSourceList)添加数据源

    public BaseAdapter(List<DS> entityList) {
        mEntityList = new ArrayList<>();
        if (Objects.nonNull(entityList)) {
            mEntityList.addAll(entityList);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        BaseViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mHeaderId, viewGroup, false);
                viewHolder = new HeaderViewHolder(view, mOnHeaderClickListener, mOnHeaderLongClickListener);
                break;
            case VIEW_TYPE_STATUS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mStatusId, viewGroup, false);
                viewHolder = new StatusViewHolder(view, mOnStatusClickListener, mOnStatusLongClickListener);
                break;
            case VIEW_TYPE_ENTITY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mEntityId, viewGroup, false);
                viewHolder = new EntityViewHolder(view, mOnEntityClickListener, mOnEntityLongClickListener);
                break;
            case VIEW_TYPE_EXTRAS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mExtrasId, viewGroup, false);
                viewHolder = new ExtrasViewHolder(view, mOnExtrasClickListener, mOnExtrasLongClickListener);
                break;
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mFooterId, viewGroup, false);
                viewHolder = new FooterViewHolder(view, mOnFooterClickListener, mOnFooterLongClickListener);
                break;
            default:
                throw new IllegalArgumentException("viewType = " + viewType);
        }
        return viewHolder;
    }//创建视图管理器

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            initHeaderView((HeaderViewHolder) viewHolder);
        } else if (viewHolder instanceof StatusViewHolder) {
            initStatusView((StatusViewHolder) viewHolder);
        } else if (viewHolder instanceof EntityViewHolder) {
            int fixEntityPosition = getFixEntityPosition(position);
            viewHolder.itemView.setTag(fixEntityPosition);
            initEntityView((EntityViewHolder) viewHolder, getEntity(fixEntityPosition), fixEntityPosition);
        } else if (viewHolder instanceof ExtrasViewHolder) {
            initExtrasView((ExtrasViewHolder) viewHolder);
        } else if (viewHolder instanceof FooterViewHolder) {
            initFooterView((FooterViewHolder) viewHolder);
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
                initEntityView((EntityViewHolder) viewHolder, getEntity(fixEntityPosition), fixEntityPosition, payloads);
            }
        }//更新item中特定View
    }//绑定数据到视图(局部刷新)

    @Override
    public int getItemViewType(int position) {
        int itemViewType = RecyclerView.INVALID_TYPE;
        if (isHeader(position)) {
            itemViewType = VIEW_TYPE_HEADER;
        } else if (isStatus(position)) {
            itemViewType = VIEW_TYPE_STATUS;
        } else if (isEntity(position)) {
            itemViewType = VIEW_TYPE_ENTITY;
        } else if (isExtras(position)) {
            itemViewType = VIEW_TYPE_EXTRAS;
        } else if (isFooter(position)) {
            itemViewType = VIEW_TYPE_FOOTER;
        }
        return itemViewType;
    }//获取ItemView的类型

    @Override
    public long getItemId(int position) {
        long i;
        if (hasStableIds() && isEntity(position)) {
            i = getEntity(getFixEntityPosition(position)).hashCode();
        } else {
            i = RecyclerView.NO_ID;
        }
        return i;
    }//为每个Item绑定唯一的Id, 需搭配adapter.setHasStableIds(true), 且要在setAdapter()前调用

    @Override
    public int getItemCount() {
        int entityViewCount = getEntityListSize();
        return mHeaderCount + mStatusCount + entityViewCount + mExtrasCount + mFooterCount;
    }//获取Item的总数

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        freeView(viewHolder);
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
        if (Objects.nonNull(viewHolder)) {
            viewHolder.itemView.callOnClick();
            result = true;
        } else {
            result = false;
        }//若ViewHolder不为null, 则表示RecyclerView布局完成; 反之, 则为未完成
        return result;
    }

    public void setHeader(@LayoutRes int layoutResId) {
        delHeader();
        addHeader(layoutResId);
    }//设置HeaderView

    public void delHeader() {
        if (mHeaderId == -1 || mHeaderCount == 0) {
            LogUtil.w("headerView not added, headerViewId = " + mHeaderId + ", headerViewCount = " + mHeaderCount);
            return;
        }
        mHeaderId = -1;
        mHeaderCount = 0;
        notifyItemRemoved(0);
    }//移除HeaderView

    public void addHeader(@LayoutRes int layoutResId) {
        if (mHeaderId != -1 || mHeaderCount != 0) {
            LogUtil.w("headerView not deleted, headerViewId = " + mHeaderId + ", headerViewCount = " + mHeaderCount);
            return;
        }
        mHeaderId = layoutResId;
        mHeaderCount = 1;
        notifyItemInserted(0);
    }//添加HeaderView

    public void setStatus(@LayoutRes int layoutResId) {
        delStatus();
        addStatus(layoutResId);
    }//设置StatusView

    public void delStatus() {
        if (mStatusId == -1 || mStatusCount == 0) {
            LogUtil.w("statusView not added, statusViewId = " + mStatusId + ", statusViewCount = " + mStatusCount);
            return;
        }
        mStatusId = -1;
        mStatusCount = 0;
        notifyItemRemoved(mHeaderCount);
    }//移除StatusView

    public void addStatus(@LayoutRes int layoutResId) {
        if (mStatusId != -1 || mStatusCount != 0) {
            LogUtil.w("statusView not deleted, statusViewId = " + mStatusId + ", statusViewCount = " + mStatusCount);
            return;
        }
        mStatusId = layoutResId;
        mStatusCount = 1;
        notifyItemInserted(mHeaderCount);
    }//添加StatusView

    public void setExtras(@LayoutRes int layoutResId) {
        delExtras();
        addExtras(layoutResId);
    }//设置ExtrasView

    public void delExtras() {
        if (mExtrasId == -1 || mExtrasCount == 0) {
            LogUtil.w("extrasView not added, extrasViewId = " + mExtrasId + ", extrasViewCount = " + mExtrasCount);
            return;
        }
        mExtrasId = -1;
        mExtrasCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderCount + mStatusCount + entityViewCount);
    }//移除ExtrasView

    public void addExtras(@LayoutRes int layoutResId) {
        if (mExtrasId != -1 || mExtrasCount != 0) {
            LogUtil.w("extrasView not deleted, extrasViewId = " + mExtrasId + ", extrasViewCount = " + mExtrasCount);
            return;
        }
        mExtrasId = layoutResId;
        mExtrasCount = 1;
        int entityViewCount = getEntityListSize();
        notifyItemInserted(mHeaderCount + mStatusCount + entityViewCount);
    }//添加ExtrasView

    public void setFooter(@LayoutRes int layoutResId) {
        delFooter();
        addFooter(layoutResId);
    }//设置FooterView

    public void delFooter() {
        if (mFooterId == -1 || mFooterCount == 0) {
            LogUtil.w("footerView not added, footerViewId = " + mFooterId + ", footerViewCount = " + mFooterCount);
            return;
        }
        mFooterId = -1;
        mFooterCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderCount + mStatusCount + entityViewCount + mExtrasCount + mFooterCount);
    }//移除FooterView

    public void addFooter(@LayoutRes int layoutResId) {
        if (mFooterId != -1 || mFooterCount != 0) {
            LogUtil.w("footerView not deleted, footerViewId = " + mFooterId + ", footerViewCount = " + mFooterCount);
            return;
        }
        mFooterId = layoutResId;
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
                mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        clickEntity(rawEntityPosition);
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);//移除监听, 避免反复回调重复点击
                    }
                });//注册RecyclerView布局完成监听器
            }
        } else {
            LogUtil.w("position = " + position + ", out of bounds");
        }
    }//编程式点击Item(无Beep音), 需要在setAdapter()后调用

    public DS getEntity(int position) {
        return mEntityList.get(position);
    }//获取数据源

    public List<DS> getEntityList() {
        return mEntityList;
    }//获取数据源集合

    public int getEntityListSize() {
        return mEntityList.size();
    }//获取数据源集合大小

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        mOnHeaderClickListener = onHeaderClickListener;
    }

    public void setOnHeaderLongClickListener(OnHeaderLongClickListener onHeaderLongClickListener) {
        mOnHeaderLongClickListener = onHeaderLongClickListener;
    }

    public void setOnStatusClickListener(OnStatusClickListener onStatusClickListener) {
        mOnStatusClickListener = onStatusClickListener;
    }

    public void setOnStatusLongClickListener(OnStatusLongClickListener onStatusLongClickListener) {
        mOnStatusLongClickListener = onStatusLongClickListener;
    }

    public void setOnEntityClickListener(OnEntityClickListener onEntityClickListener) {
        mOnEntityClickListener = onEntityClickListener;
    }

    public void setOnEntityLongClickListener(OnEntityLongClickListener onEntityLongClickListener) {
        mOnEntityLongClickListener = onEntityLongClickListener;
    }

    public void setOnExtrasClickListener(OnExtrasClickListener onExtrasClickListener) {
        mOnExtrasClickListener = onExtrasClickListener;
    }

    public void setOnExtrasLongClickListener(OnExtrasLongClickListener onExtrasLongClickListener) {
        mOnExtrasLongClickListener = onExtrasLongClickListener;
    }

    public void setOnFooterClickListener(OnFooterClickListener onFooterClickListener) {
        mOnFooterClickListener = onFooterClickListener;
    }

    public void setOnFooterLongClickListener(OnFooterLongClickListener onFooterLongClickListener) {
        mOnFooterLongClickListener = onFooterLongClickListener;
    }

    protected Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    protected abstract int getLayoutRes();

    protected void initHeaderView(HeaderViewHolder viewHolder) {
    }

    protected void initStatusView(StatusViewHolder viewHolder) {
    }

    protected void initEntityView(EntityViewHolder viewHolder, DS dataSource, int position) {
    }

    protected void initEntityView(EntityViewHolder viewHolder, DS dataSource, int position, List<Object> payloads) {
    }

    protected void initExtrasView(ExtrasViewHolder viewHolder) {
    }

    protected void initFooterView(FooterViewHolder viewHolder) {
    }

    protected void freeView(BaseViewHolder viewHolder) {
    }
}
