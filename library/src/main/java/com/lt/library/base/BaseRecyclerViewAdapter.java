package com.lt.library.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lt.library.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2018/12/12 12:10
 * @版本: 1.0
 * @描述: //BaseRecyclerViewAdapter, 注意引入依赖
 * 源址: https://blog.csdn.net/a_zhon/article/details/66971369
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public abstract class BaseRecyclerViewAdapter<DS> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_STATUS = 2;
    private static final int VIEW_TYPE_ENTITY = 3;
    private static final int VIEW_TYPE_EXTRAS = 4;
    private static final int VIEW_TYPE_FOOTER = 5;
    private static int mHeaderViewCount = 0;
    private static int mStatusViewCount = 0;
    private static int mExtrasViewCount = 0;
    private static int mFooterViewCount = 0;
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
    private List<DS> mEntityList;
    private int mHeaderViewId = -1;
    private int mStatusViewId = -1;
    private int mEntityViewId = setLayoutResId();
    private int mExtrasViewId = -1;
    private int mFooterViewId = -1;

    public BaseRecyclerViewAdapter(List<DS> entityList) {
        if (Objects.isNull(mEntityList)) {
            mEntityList = new ArrayList<>();
        } else {
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mHeaderViewId, viewGroup, false);
                viewHolder = new HeaderViewHolder(view, mOnHeaderClickListener, mOnHeaderLongClickListener);
                break;
            case VIEW_TYPE_STATUS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mStatusViewId, viewGroup, false);
                viewHolder = new StatusViewHolder(view, mOnStatusClickListener, mOnStatusLongClickListener);
                break;
            case VIEW_TYPE_ENTITY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mEntityViewId, viewGroup, false);
                viewHolder = new EntityViewHolder(view, mOnEntityClickListener, mOnEntityLongClickListener);
                break;
            case VIEW_TYPE_EXTRAS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mExtrasViewId, viewGroup, false);
                viewHolder = new ExtrasViewHolder(view, mOnExtrasClickListener, mOnExtrasLongClickListener);
                break;
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mFooterViewId, viewGroup, false);
                viewHolder = new FooterViewHolder(view, mOnFooterClickListener, mOnFooterLongClickListener);
                break;
            default:
                throw new RuntimeException("viewType invalid");
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
            int fixPosition = position - (mHeaderViewCount + mStatusViewCount);
            viewHolder.itemView.setTag(fixPosition);
            initEntityView((EntityViewHolder) viewHolder, mEntityList.get(fixPosition), fixPosition);
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
                int fixPosition = position - (mHeaderViewCount + mStatusViewCount);
                updatePartEntityView((EntityViewHolder) viewHolder, mEntityList.get(fixPosition), fixPosition, payloads);
            }
        }//更新item中特定View
    }//绑定数据到视图(局部刷新)

    @Override
    public int getItemViewType(int position) {
        int itemViewType = RecyclerView.INVALID_TYPE;
        if (isHeaderView(position)) {
            itemViewType = VIEW_TYPE_HEADER;
        } else if (isStatusView(position)) {
            itemViewType = VIEW_TYPE_STATUS;
        } else if (isEntityView(position)) {
            itemViewType = VIEW_TYPE_ENTITY;
        } else if (isExtrasView(position)) {
            itemViewType = VIEW_TYPE_EXTRAS;
        } else if (isFooterView(position)) {
            itemViewType = VIEW_TYPE_FOOTER;
        }
        return itemViewType;
    }//获取ItemView的类型

    @Override
    public long getItemId(int position) {
        long i;
        if (hasStableIds() && isEntityView(position)) {
            i = mEntityList.get(position - (mHeaderViewCount + mStatusViewCount)).hashCode();
        } else {
            i = RecyclerView.NO_ID;
        }
        return i;
    }//为每个Item绑定唯一的Id, 需搭配adapter.setHasStableIds(true), 且要在setAdapter()前调用

    @Override
    public int getItemCount() {
        int entityViewCount = getEntityListSize();
        return mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount + mFooterViewCount;
    }//获取Item的总数

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder instanceof EntityViewHolder) {
            freeView((EntityViewHolder) viewHolder);
        }
    }//Item回收时回调

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        setStGridLayoutManagerFullSpan(viewHolder);//当LayoutManager为StaggeredGridLayoutManager时, 让非EntityView占满所在行
    }//Item滑入屏幕时回调

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
    }//Item滑出屏幕时回调

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setGridLayoutManagerFullSpan(recyclerView);//当LayoutManager为GridLayoutManager时, 让非EntityView占满所在行
    }//setAdapter()后新的Adapter回调

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }//setAdapter()后旧的Adapter回调

    private boolean isHeaderView(int position) {
        return mHeaderViewCount > 0
                && position < mHeaderViewCount;
    }

    private boolean isStatusView(int position) {
        return mStatusViewCount > 0
                && mHeaderViewCount <= position
                && position < mHeaderViewCount + mStatusViewCount;
    }

    private boolean isEntityView(int position) {
        int entityViewCount = getEntityListSize();
        return entityViewCount > 0
                && mHeaderViewCount + mStatusViewCount <= position
                && position < mHeaderViewCount + mStatusViewCount + entityViewCount;
    }

    private boolean isExtrasView(int position) {
        int entityViewCount = getEntityListSize();
        return mExtrasViewCount > 0
                && mHeaderViewCount + mStatusViewCount + entityViewCount <= position
                && position < mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount;
    }

    private boolean isFooterView(int position) {
        int entityViewCount = getEntityListSize();
        return mFooterViewCount > 0
                && mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount <= position;
    }

    private void setStGridLayoutManagerFullSpan(@NonNull BaseViewHolder viewHolder) {
        ViewGroup.LayoutParams viewGroupLayoutParams = viewHolder.itemView.getLayoutParams();
        if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sgLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
            if (!isEntityView(viewHolder.getLayoutPosition())) {
                sgLayoutManagerLayoutParams.setFullSpan(true);
            }
        }
    }

    private void setGridLayoutManagerFullSpan(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager rcvLayoutManager = recyclerView.getLayoutManager();
        if (rcvLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) rcvLayoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (!isEntityView(position)) {
                        spanSize = gridLayoutManager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }
    }

    public void setHeaderView(@LayoutRes int layoutResId) {
        delHeaderView();
        addHeaderView(layoutResId);
    }//设置HeaderView

    public void delHeaderView() {
        if (mHeaderViewId == -1 || mHeaderViewCount == 0) {
            LogUtil.w("headerView not added, headerViewId = " + mHeaderViewId + ", headerViewCount = " + mHeaderViewCount);
            return;
        }
        mHeaderViewId = -1;
        mHeaderViewCount = 0;
        notifyItemRemoved(0);
    }//移除HeaderView

    public void addHeaderView(@LayoutRes int layoutResId) {
        if (mHeaderViewId != -1 || mHeaderViewCount != 0) {
            LogUtil.w("headerView not deleted, headerViewId = " + mHeaderViewId + ", headerViewCount = " + mHeaderViewCount);
            return;
        }
        mFooterViewId = layoutResId;
        mFooterViewCount = 1;
        notifyItemInserted(0);
    }//添加HeaderView

    public void setStatusView(@LayoutRes int layoutResId) {
        delStatusView();
        addStatusView(layoutResId);
    }//设置StatusView

    public void delStatusView() {
        if (mStatusViewId == -1 || mStatusViewCount == 0) {
            LogUtil.w("statusView not added, statusViewId = " + mStatusViewId + ", statusViewCount = " + mStatusViewCount);
            return;
        }
        mStatusViewId = -1;
        mStatusViewCount = 0;
        notifyItemRemoved(mHeaderViewCount);
    }//移除StatusView

    public void addStatusView(@LayoutRes int layoutResId) {
        if (mStatusViewId != -1 || mStatusViewCount != 0) {
            LogUtil.w("statusView not deleted, statusViewId = " + mStatusViewId + ", statusViewCount = " + mStatusViewCount);
            return;
        }
        mStatusViewId = layoutResId;
        mStatusViewCount = 1;
        notifyItemInserted(mHeaderViewCount);
    }//添加StatusView

    public void setExtrasView(@LayoutRes int layoutResId) {
        delExtrasView();
        addExtrasView(layoutResId);
    }//设置ExtrasView

    public void delExtrasView() {
        if (mExtrasViewId == -1 || mExtrasViewCount == 0) {
            LogUtil.w("extrasView not added, extrasViewId = " + mExtrasViewId + ", extrasViewCount = " + mExtrasViewCount);
            return;
        }
        mExtrasViewId = -1;
        mExtrasViewCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderViewCount + mStatusViewCount + entityViewCount);
    }//移除ExtrasView

    public void addExtrasView(@LayoutRes int layoutResId) {
        if (mExtrasViewId != -1 || mExtrasViewCount != 0) {
            LogUtil.w("extrasView not deleted, extrasViewId = " + mExtrasViewId + ", extrasViewCount = " + mExtrasViewCount);
            return;
        }
        mExtrasViewId = layoutResId;
        mExtrasViewCount = 1;
        int entityViewCount = getEntityListSize();
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + entityViewCount);
    }//添加ExtrasView

    public void setFooterView(@LayoutRes int layoutResId) {
        delFooterView();
        addFooterView(layoutResId);
    }//设置FooterView

    public void delFooterView() {
        if (mFooterViewId == -1 || mFooterViewCount == 0) {
            LogUtil.w("footerView not added, footerViewId = " + mFooterViewId + ", footerViewCount = " + mFooterViewCount);
            return;
        }
        mFooterViewId = -1;
        mFooterViewCount = 0;
        int entityViewCount = getEntityListSize();
        notifyItemRemoved(mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount + mFooterViewCount);
    }//移除FooterView

    public void addFooterView(@LayoutRes int layoutResId) {
        if (mFooterViewId != -1 || mFooterViewCount != 0) {
            LogUtil.w("footerView not deleted, footerViewId = " + mFooterViewId + ", footerViewCount = " + mFooterViewCount);
            return;
        }
        mFooterViewId = layoutResId;
        mFooterViewCount = 1;
        int entityViewCount = getEntityListSize();
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount + mFooterViewCount);
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
        notifyItemRangeRemoved(mHeaderViewCount + mStatusViewCount, entityViewCount);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount, entityViewCount);
    }//清空数据源集合, 并更新item

    public void notifyEntityAddAll(List<DS> dataSourceList) {
        int entityViewCount = getEntityListSize();
        mEntityList.addAll(dataSourceList);
        notifyItemRangeInserted(mHeaderViewCount + mStatusViewCount + entityViewCount, dataSourceList.size());
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + entityViewCount, dataSourceList.size());
    }//添加数据源集合, 并更新item

    public void notifyEntityRef(DS dataSource, int position) {
        mEntityList.set(position, dataSource);
        notifyItemChanged(mHeaderViewCount + mStatusViewCount + position);
    }//刷新数据源, 并更新item

    public void notifyEntityDel(int position) {
        int entityViewCount = getEntityListSize();
        mEntityList.remove(position);
        notifyItemRemoved(mHeaderViewCount + mStatusViewCount + position);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + position, entityViewCount - position);
    }//清空数据源, 并更新item

    public void notifyEntityAdd(DS dataSource, int position) {
        int entityViewCount = getEntityListSize();
        mEntityList.add(position, dataSource);
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + position);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + position, entityViewCount - position);
    }//添加数据源, 并更新item

    public DS getEntity(int position) {
        return mEntityList.get(position);
    }//获取数据源

    public int getEntityListSize() {
        return mEntityList.size();
    }//获取数据源大小

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

    protected abstract int setLayoutResId();

    protected abstract void initHeaderView(HeaderViewHolder viewHolder);

    protected abstract void initStatusView(StatusViewHolder viewHolder);

    protected abstract void initEntityView(EntityViewHolder viewHolder, DS dataSource, int position);

    protected abstract void updatePartEntityView(EntityViewHolder viewHolder, DS dataSource, int position, List<Object> payloads);

    protected abstract void initExtrasView(ExtrasViewHolder viewHolder);

    protected abstract void initFooterView(FooterViewHolder viewHolder);

    protected abstract void freeView(EntityViewHolder viewHolder);

    public interface OnHeaderClickListener {
        void onHeaderClick(View view);
    }

    public interface OnHeaderLongClickListener {
        boolean onHeaderLongClick(View view);
    }

    public interface OnStatusClickListener {
        void onStatusClick(View view);
    }

    public interface OnStatusLongClickListener {
        boolean onStatusLongClick(View view);
    }

    public interface OnEntityClickListener {
        void onEntityClick(View view, int position);
    }

    public interface OnEntityLongClickListener {
        boolean onEntityLongClick(View view, int position);
    }

    public interface OnExtrasClickListener {
        void onExtrasClick(View view);
    }

    public interface OnExtrasLongClickListener {
        boolean onExtrasLongClick(View view);
    }

    public interface OnFooterClickListener {
        void onFooterClick(View view);
    }

    public interface OnFooterLongClickListener {
        boolean onFooterLongClick(View view);
    }

    protected static class HeaderViewHolder extends BaseViewHolder {

        private HeaderViewHolder(@NonNull View itemView, OnHeaderClickListener onHeaderClickListener, OnHeaderLongClickListener onHeaderLongClickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onHeaderClickListener != null) {
                    onHeaderClickListener.onHeaderClick(view);
                }
            });
            itemView.setOnLongClickListener(view -> {
                boolean b = false;
                if (onHeaderLongClickListener != null) {
                    b = onHeaderLongClickListener.onHeaderLongClick(view);
                }
                return b;
            });
        }
    }

    protected static class StatusViewHolder extends BaseViewHolder {

        private StatusViewHolder(@NonNull View itemView, OnStatusClickListener onStatusClickListener, OnStatusLongClickListener onStatusLongClickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onStatusClick(view);
                }
            });
            itemView.setOnLongClickListener(view -> {
                boolean b = false;
                if (onStatusLongClickListener != null) {
                    b = onStatusLongClickListener.onStatusLongClick(view);
                }
                return b;
            });
        }
    }

    protected static class EntityViewHolder extends BaseViewHolder {

        private EntityViewHolder(@NonNull View itemView, OnEntityClickListener onEntityClickListener, OnEntityLongClickListener onEntityLongClickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onEntityClickListener != null) {
                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        LogUtil.w("view is changing");
                        return;
                    }
                    onEntityClickListener.onEntityClick(view, position - (mHeaderViewCount + mStatusViewCount));
                }
            });
            itemView.setOnLongClickListener(view -> {
                boolean b = false;
                if (onEntityLongClickListener != null) {
                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        LogUtil.w("view is changing");
                        return false;
                    }
                    b = onEntityLongClickListener.onEntityLongClick(view, position - (mHeaderViewCount + mStatusViewCount));
                }
                return b;
            });
        }
    }

    protected static class ExtrasViewHolder extends BaseViewHolder {

        private ExtrasViewHolder(@NonNull View itemView, OnExtrasClickListener onExtrasClickListener, OnExtrasLongClickListener onExtrasLongClickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onExtrasClickListener != null) {
                    onExtrasClickListener.onExtrasClick(view);
                }
            });
            itemView.setOnLongClickListener(view -> {
                boolean b = false;
                if (onExtrasLongClickListener != null) {
                    b = onExtrasLongClickListener.onExtrasLongClick(view);
                }
                return b;
            });
        }
    }

    protected static class FooterViewHolder extends BaseViewHolder {

        private FooterViewHolder(@NonNull View itemView, OnFooterClickListener onFooterClickListener, OnFooterLongClickListener onFooterLongClickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onFooterClickListener != null) {
                    onFooterClickListener.onFooterClick(view);
                }
            });
            itemView.setOnLongClickListener(view -> {
                boolean b = false;
                if (onFooterLongClickListener != null) {
                    b = onFooterLongClickListener.onFooterLongClick(view);
                }
                return b;
            });
        }
    }
}
