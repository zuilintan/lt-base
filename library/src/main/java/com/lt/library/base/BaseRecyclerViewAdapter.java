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
    private static final int VIEW_TYPE_HEADER = 2020;
    private static final int VIEW_TYPE_STATUS = 2021;
    private static final int VIEW_TYPE_ENTITY = 2022;
    private static final int VIEW_TYPE_EXTRAS = 2023;
    private static final int VIEW_TYPE_FOOTER = 2024;
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
    private int mHeaderViewId = -1;
    private int mStatusViewId = -1;
    private int mEntityViewId = setLayoutResId();
    private int mExtrasViewId = -1;
    private int mFooterViewId = -1;
    private List<DS> mDataSourceList;
    private boolean mIsHeaderFullSpan = true;
    private boolean mIsFooterFullSpan = true;

    public BaseRecyclerViewAdapter(List<DS> dataSourceList) {
        if (Objects.isNull(mDataSourceList)) {
            mDataSourceList = new ArrayList<>();
        } else {
            mDataSourceList.addAll(dataSourceList);
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
                HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
                headerViewHolder.setOnHeaderClickListener(mOnHeaderClickListener);
                headerViewHolder.setOnHeaderLongClickListener(mOnHeaderLongClickListener);
                viewHolder = headerViewHolder;
                break;
            case VIEW_TYPE_STATUS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mStatusViewId, viewGroup, false);
                StatusViewHolder statusViewHolder = new StatusViewHolder(view);
                statusViewHolder.setOnStatusClickListener(mOnStatusClickListener);
                statusViewHolder.setOnStatusLongClickListener(mOnStatusLongClickListener);
                viewHolder = statusViewHolder;
                break;
            case VIEW_TYPE_ENTITY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mEntityViewId, viewGroup, false);
                EntityViewHolder entityViewHolder = new EntityViewHolder(view);
                entityViewHolder.setOnEntityClickListener(mOnEntityClickListener);
                entityViewHolder.setOnEntityLongClickListener(mOnEntityLongClickListener);
                viewHolder = entityViewHolder;
                break;
            case VIEW_TYPE_EXTRAS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mExtrasViewId, viewGroup, false);
                ExtrasViewHolder extrasViewHolder = new ExtrasViewHolder(view);
                extrasViewHolder.setOnExtrasClickListener(mOnExtrasClickListener);
                extrasViewHolder.setOnExtrasLongClickListener(mOnExtrasLongClickListener);
                viewHolder = extrasViewHolder;
                break;
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(mFooterViewId, viewGroup, false);
                FooterViewHolder footerViewHolder = new FooterViewHolder(view);
                footerViewHolder.setOnFooterClickListener(mOnFooterClickListener);
                footerViewHolder.setOnFooterLongClickListener(mOnFooterLongClickListener);
                viewHolder = footerViewHolder;
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
            initEntityView((EntityViewHolder) viewHolder, mDataSourceList.get(fixPosition), fixPosition);
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
                updatePartEntityView((EntityViewHolder) viewHolder, mDataSourceList.get(fixPosition), fixPosition, payloads);
            }
        }//更新item中特定View
    }//绑定数据到视图(局部刷新)

    @Override
    public int getItemViewType(int position) {
        int itemViewType = 0;
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
            i = mDataSourceList.get(position - (mHeaderViewCount + mStatusViewCount)).hashCode();
        } else {
            i = RecyclerView.NO_ID;
        }
        return i;
    }//为每个Item绑定唯一的Id, 需搭配adapter.setHasStableIds(true), 且要在setAdapter()前调用

    @Override
    public int getItemCount() {
        int entityViewCount = size();
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
        int position = viewHolder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            LogUtil.w("view is changing");
            return;
        }
        if ((mIsHeaderFullSpan && isHeaderView(position)) || (mIsFooterFullSpan && isFooterView(position))) {
            setSgLayoutManagerFullSpan(viewHolder);//当LayoutManager为StaggeredGridLayoutManager时, 让HeaderView或FootView占满所在行
        }
    }//Item滑入屏幕时回调

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
    }//Item滑出屏幕时回调

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mIsHeaderFullSpan || mIsFooterFullSpan) {
            setGridLayoutManagerFullSpan(recyclerView);//当LayoutManager为GridLayoutManager时, 让HeaderView或FootView占满所在行
        }
    }//setAdapter()后新的Adapter回调

    private void setSgLayoutManagerFullSpan(@NonNull BaseViewHolder viewHolder) {
        ViewGroup.LayoutParams viewGroupLayoutParams = viewHolder.itemView.getLayoutParams();
        if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sgLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
            sgLayoutManagerLayoutParams.setFullSpan(true);
        }
    }

    private void setGridLayoutManagerFullSpan(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager rcvLayoutManager = recyclerView.getLayoutManager();
        if (rcvLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) rcvLayoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize;
                    if (isHeaderView(position) || isFooterView(position)) {
                        spanSize = gridLayoutManager.getSpanCount();
                    } else {
                        spanSize = 1;
                    }
                    return spanSize;
                }
            });
        }
    }

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
        int entityViewCount = size();
        return entityViewCount > 0
                && mHeaderViewCount + mStatusViewCount <= position
                && position < mHeaderViewCount + mStatusViewCount + entityViewCount;
    }

    private boolean isExtrasView(int position) {
        int entityViewCount = size();
        return mExtrasViewCount > 0
                && mHeaderViewCount + mStatusViewCount + entityViewCount <= position
                && position < mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount;
    }

    private boolean isFooterView(int position) {
        int entityViewCount = size();
        return mFooterViewCount > 0
                && mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount <= position;
    }

    public void setHeaderView(@LayoutRes int layoutResId) {
        setHeaderView(layoutResId, true);
    }//设置HeaderView

    public void setHeaderView(@LayoutRes int layoutResId, boolean isFullSpan) {
        mHeaderViewId = layoutResId;
        mIsHeaderFullSpan = isFullSpan;
        mHeaderViewCount = 1;
        notifyItemInserted(0);
    }//设置HeaderView

    public void addStatusView(@LayoutRes int layoutResId) {
        mStatusViewId = layoutResId;
        mStatusViewCount = 1;
        notifyItemInserted(mHeaderViewCount);
    }//添加StatusView

    public void remStatusView() {
        mStatusViewId = -1;
        mStatusViewCount = 0;
        notifyItemRemoved(mHeaderViewCount);
    }//移除StatusView

    public void addExtrasView(@LayoutRes int layoutResId) {
        mExtrasViewId = layoutResId;
        mExtrasViewCount = 1;
        int entityViewCount = size();
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + entityViewCount);
    }//添加ExtrasView

    public void remExtrasView() {
        mExtrasViewId = -1;
        mExtrasViewCount = 0;
        int entityViewCount = size();
        notifyItemRemoved(mHeaderViewCount + mStatusViewCount + entityViewCount);
    }//移除ExtrasView

    public void setFooterView(@LayoutRes int layoutResId) {
        setFooterView(layoutResId, true);
    }//设置FooterView

    public void setFooterView(@LayoutRes int layoutResId, boolean isFullSpan) {
        mFooterViewId = layoutResId;
        mIsFooterFullSpan = isFullSpan;
        mFooterViewCount = 1;
        int entityViewCount = size();
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + entityViewCount + mExtrasViewCount + mFooterViewCount);
    }//设置FooterView

    public void refAllWithNoAnim(List<DS> dataSourceList) {
        if (!mDataSourceList.isEmpty()) {
            mDataSourceList.clear();
        }
        mDataSourceList.addAll(dataSourceList);
        notifyDataSetChanged();
    }//刷新数据源集合, 并更新item, 性能较低, 无动画

    public void refAll(List<DS> dataSourceList) {
        if (!mDataSourceList.isEmpty()) {
            remAll();
        }
        addAll(dataSourceList);
    }//刷新数据源集合, 并更新item

    public void remAll() {
        int entityViewCount = size();
        mDataSourceList.clear();
        notifyItemRangeRemoved(mHeaderViewCount + mStatusViewCount, entityViewCount);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount, entityViewCount);
    }//清空数据源集合, 并更新item

    public void addAll(List<DS> dataSourceList) {
        int entityViewCount = size();
        mDataSourceList.addAll(dataSourceList);
        notifyItemRangeInserted(mHeaderViewCount + mStatusViewCount + entityViewCount, dataSourceList.size());
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + entityViewCount, dataSourceList.size());
    }//添加数据源集合, 并更新item

    public void ref(DS dataSource, int position) {
        mDataSourceList.set(position, dataSource);
        notifyItemChanged(mHeaderViewCount + mStatusViewCount + position);
    }//刷新数据源, 并更新item

    public void rem(int position) {
        int entityViewCount = size();
        mDataSourceList.remove(position);
        notifyItemRemoved(mHeaderViewCount + mStatusViewCount + position);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + position, entityViewCount - position);
    }//清空数据源, 并更新item

    public void add(DS dataSource, int position) {
        int entityViewCount = size();
        mDataSourceList.add(position, dataSource);
        notifyItemInserted(mHeaderViewCount + mStatusViewCount + position);
        notifyItemRangeChanged(mHeaderViewCount + mStatusViewCount + position, entityViewCount - position);
    }//添加数据源, 并更新item

    public DS get(int position) {
        return mDataSourceList.get(position);
    }//获取数据源

    public int size() {
        return mDataSourceList.size();
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

    protected static class HeaderViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnHeaderClickListener mOnHeaderClickListener;
        private OnHeaderLongClickListener mOnHeaderLongClickListener;

        private HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnHeaderClickListener != null) {
                mOnHeaderClickListener.onHeaderClick(view);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            boolean b = false;
            if (mOnHeaderLongClickListener != null) {
                b = mOnHeaderLongClickListener.onHeaderLongClick(view);
            }
            return b;
        }

        private void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
            mOnHeaderClickListener = onHeaderClickListener;
        }

        private void setOnHeaderLongClickListener(OnHeaderLongClickListener onHeaderLongClickListener) {
            mOnHeaderLongClickListener = onHeaderLongClickListener;
        }
    }

    protected static class StatusViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnStatusClickListener mOnStatusClickListener;
        private OnStatusLongClickListener mOnStatusLongClickListener;

        private StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnStatusClickListener != null) {
                mOnStatusClickListener.onStatusClick(view);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            boolean b = false;
            if (mOnStatusLongClickListener != null) {
                b = mOnStatusLongClickListener.onStatusLongClick(view);
            }
            return b;
        }

        private void setOnStatusClickListener(OnStatusClickListener onStatusClickListener) {
            mOnStatusClickListener = onStatusClickListener;
        }

        private void setOnStatusLongClickListener(OnStatusLongClickListener onStatusLongClickListener) {
            mOnStatusLongClickListener = onStatusLongClickListener;
        }
    }

    protected static class EntityViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnEntityClickListener mOnEntityClickListener;
        private OnEntityLongClickListener mOnEntityLongClickListener;

        private EntityViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnEntityClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return;
                }
                mOnEntityClickListener.onEntityClick(view, position - (mHeaderViewCount + mStatusViewCount));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            boolean b = false;
            if (mOnEntityLongClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return false;
                }
                b = mOnEntityLongClickListener.onEntityLongClick(view, position - (mHeaderViewCount + mStatusViewCount));
            }
            return b;
        }

        private void setOnEntityClickListener(OnEntityClickListener onEntityClickListener) {
            mOnEntityClickListener = onEntityClickListener;
        }

        private void setOnEntityLongClickListener(OnEntityLongClickListener onEntityLongClickListener) {
            mOnEntityLongClickListener = onEntityLongClickListener;
        }
    }

    protected static class ExtrasViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnExtrasClickListener mOnExtrasClickListener;
        private OnExtrasLongClickListener mOnExtrasLongClickListener;

        private ExtrasViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnExtrasClickListener != null) {
                mOnExtrasClickListener.onExtrasClick(view);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            boolean b = false;
            if (mOnExtrasLongClickListener != null) {
                b = mOnExtrasLongClickListener.onExtrasLongClick(view);
            }
            return b;
        }

        private void setOnExtrasClickListener(OnExtrasClickListener onExtrasClickListener) {
            mOnExtrasClickListener = onExtrasClickListener;
        }

        private void setOnExtrasLongClickListener(OnExtrasLongClickListener onExtrasLongClickListener) {
            mOnExtrasLongClickListener = onExtrasLongClickListener;
        }
    }

    protected static class FooterViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnFooterClickListener mOnFooterClickListener;
        private OnFooterLongClickListener mOnFooterLongClickListener;

        private FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnFooterClickListener != null) {
                mOnFooterClickListener.onFooterClick(view);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            boolean b = false;
            if (mOnFooterLongClickListener != null) {
                b = mOnFooterLongClickListener.onFooterLongClick(view);
            }
            return b;
        }

        private void setOnFooterClickListener(OnFooterClickListener onFooterClickListener) {
            mOnFooterClickListener = onFooterClickListener;
        }

        private void setOnFooterLongClickListener(OnFooterLongClickListener onFooterLongClickListener) {
            mOnFooterLongClickListener = onFooterLongClickListener;
        }
    }
}
