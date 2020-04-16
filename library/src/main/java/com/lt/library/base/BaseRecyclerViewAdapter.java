package com.lt.library.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2018/12/12 12:10
 * @版本: 1.0
 * @描述: //RecyclerView的封装类。注意引入依赖。
 * 源址: https://blog.csdn.net/a_zhon/article/details/66971369
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public abstract class BaseRecyclerViewAdapter<DS, VH extends ViewHolder> extends RecyclerView.Adapter<VH>
        implements OnClickListener, OnLongClickListener {//DS:DataSource，VH:ViewHolder
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_HEAD = 1;
    private final int VIEW_TYPE_FOOT = 2;
    private int mItemViewId;
    private int mHeadViewId = -1;
    private int mFootViewId = -1;
    private List<DS> mDataSourceList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerViewAdapter(@LayoutRes int layoutResId, List<DS> dataSourceList) {
        if (layoutResId != 0) {
            mItemViewId = layoutResId;
        }
        if (dataSourceList != null) {
            mDataSourceList.addAll(dataSourceList);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                                     .inflate(mItemViewId, viewGroup, false);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                break;
            case VIEW_TYPE_HEAD:
                view = LayoutInflater.from(viewGroup.getContext())
                                     .inflate(mHeadViewId, viewGroup, false);
                break;
            case VIEW_TYPE_FOOT:
                view = LayoutInflater.from(viewGroup.getContext())
                                     .inflate(mFootViewId, viewGroup, false);
                break;
            default:
                break;
        }
        return (VH) new BaseViewHolder(Objects.requireNonNull(view));
    }//创建视图管理器

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                if (mHeadViewId == -1) {
                    viewHolder.itemView.setTag(position);
                    initView(viewHolder, mDataSourceList.get(position),
                             position);
                }//无HeadView时的处理
                else {
                    viewHolder.itemView.setTag(position - 1);
                    initView(viewHolder, mDataSourceList.get(position - 1),
                             (position - 1));
                }//有HeadView时的处理

                break;
            case VIEW_TYPE_HEAD:
                break;
            case VIEW_TYPE_FOOT:
                break;
            default:
                break;
        }
    }//绑定数据到视图

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads);
        }//更新整个item
        else {
            if (mHeadViewId == -1) {
                localUpdateView(viewHolder, mDataSourceList.get(position), position, payloads);
            }//无HeadView时的处理
            else {
                localUpdateView(viewHolder, mDataSourceList.get(position - 1), position - 1, payloads);
            }//有HeadView时的处理
        }//仅更新item中指定的View
    }//绑定数据到视图(局部刷新)

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }//为每个Item绑定唯一的Id，可以返回position, 但需搭配adapter.setHasStableIds(true)

    @Override
    public int getItemCount() {
        if (mHeadViewId != -1 && mFootViewId != -1) {
            return mDataSourceList.size() + 2;
        }//若同时添加HeadView与FootView，数据源大小+2
        if (mHeadViewId != -1 || mFootViewId != -1) {
            return mDataSourceList.size() + 1;
        }//若仅添加HeadView或FootView其一，数据源大小+1
        return mDataSourceList.size();//仅有ItemView，数据源大小正常
    }//获取数据源的大小

    @Override
    public int getItemViewType(int position) {
        int viewType = VIEW_TYPE_ITEM;
        if (position == 0 && mHeadViewId != -1) {
            viewType = VIEW_TYPE_HEAD;
        }//判断是否添加了HeadView
        if (position == getItemCount() - 1 && mFootViewId != -1) {
            viewType = VIEW_TYPE_FOOT;
        }//判断是否添加了FootView
        return viewType;
    }//获取ItemView的类型

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager viewLayoutManager = recyclerView.getLayoutManager();
        if (viewLayoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) viewLayoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize;
                    switch (getItemViewType(position)) {
                        case VIEW_TYPE_HEAD:
                        case VIEW_TYPE_FOOT:
                            spanSize = gridLayoutManager.getSpanCount();
                            break;
                        default:
                            spanSize = 1;
                            break;
                    }
                    return spanSize;
                }
            });
        }
    }//当LayoutManager为GridLayoutManager时，让HeadView与FootView占满所在行

    @Override
    public void onViewAttachedToWindow(@NonNull VH viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        ViewGroup.LayoutParams viewGroupLayoutParams = viewHolder.itemView.getLayoutParams();
        if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglManagerLayoutParams
                    = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
            switch (getItemViewType(viewHolder.getLayoutPosition())) {
                case VIEW_TYPE_HEAD:
                case VIEW_TYPE_FOOT:
                    sglManagerLayoutParams.setFullSpan(true);
                    break;
                default:
                    break;
            }
        }
    }//当LayoutManager为StaggeredGridLayoutManager时，让HeadView与FootView占满所在行

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
    }//Item滑出屏幕时回调

    @Override
    public void onViewRecycled(@NonNull VH viewHolder) {
        super.onViewRecycled(viewHolder);
        freeView(viewHolder);
    }//Item回收时回调

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }//点击回调

    @Override
    public boolean onLongClick(View view) {
        boolean b = false;
        if (mOnItemLongClickListener != null) {
            b = mOnItemLongClickListener.onItemLongClick(view, (Integer) view.getTag());
        }
        return b;
    }//长点击回调

    public void addHeadView(@LayoutRes int layoutResId) {
        mHeadViewId = layoutResId;
    }//添加HeadView

    public void addFootView(@LayoutRes int layoutResId) {
        mFootViewId = layoutResId;
    }//添加FootView

    public List<DS> getDataSourceList() {
        return mDataSourceList;
    }

    public void setDataSourceList(List<DS> newDataSourceList) {
        mDataSourceList.clear();
        mDataSourceList.addAll(newDataSourceList);
    }

    public void add(DS dataSource, int position) {
        int size = mDataSourceList.size();
        mDataSourceList.add(position, dataSource);
        if (mHeadViewId == -1) {
            notifyItemInserted(position);
            notifyItemRangeChanged(size, 0);
        } else {
            notifyItemInserted(position + 1);
            notifyItemRangeChanged(size + 1, 0);
        }
    }//添加数据源到指定位置，并更新item

    public void del(int position) {
        int size = mDataSourceList.size();
        mDataSourceList.remove(position);
        if (mHeadViewId == -1) {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,
                                   size - position);
        } else {
            notifyItemRemoved(position + 1);
            notifyItemRangeChanged(position + 1,
                                   size - position);
        }
    }//删除指定位置的数据源，并更新item

    public void update(DS dataSource, int position) {
        mDataSourceList.set(position, dataSource);
        if (mHeadViewId == -1) {
            notifyItemChanged(position);
        } else {
            notifyItemChanged(position + 1);
        }
    }//更新指定位置的数据源，并可以单独更新其中的某个View。需与update()联用

    public void clear() {
        int size = mDataSourceList.size();
        mDataSourceList.clear();
        if (mHeadViewId == -1) {
            notifyItemRangeRemoved(0, size);
            notifyItemRangeChanged(0, size);
        } else {
            notifyItemRangeRemoved(1, size);
            notifyItemRangeChanged(1, size);
        }
    }//清空数据源集合，并更新item

    public void refresh(List<DS> dataSourceList) {
        if (mDataSourceList.isEmpty()) {
            loadMore(dataSourceList);
        } else {
            int size = mDataSourceList.size();
            mDataSourceList.clear();
            if (mHeadViewId == -1) {
                notifyItemRangeRemoved(0, size);
                notifyItemRangeChanged(0, size);
                mDataSourceList.addAll(dataSourceList);
                notifyItemRangeInserted(0, dataSourceList.size());
                notifyItemRangeChanged(0, dataSourceList.size());
            } else {
                notifyItemRangeRemoved(1, size);
                notifyItemRangeChanged(1, size);
                mDataSourceList.addAll(dataSourceList);
                notifyItemRangeInserted(1, dataSourceList.size());
                notifyItemRangeChanged(1, dataSourceList.size());
            }
        }
    }//刷新数据源集合，并更新item

    public void loadMore(List<DS> dataSourceList) {
        int size = mDataSourceList.size();
        mDataSourceList.addAll(dataSourceList);
        if (mHeadViewId == -1) {
            notifyItemRangeInserted(size, dataSourceList.size());
            notifyItemRangeChanged(size, dataSourceList.size());
        } else {
            notifyItemRangeInserted(size + 1, dataSourceList.size());
            notifyItemRangeChanged(size + 1, dataSourceList.size());
        }
    }//加载更多数据到数据源集合，并更新item

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }//item点击监听器

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }//item长点击监听器

    protected abstract void initView(VH vh, DS ds, int position);

    protected abstract void freeView(VH vh);

    protected abstract void localUpdateView(VH vh, DS ds, int position, List<Object> payloads);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }//item点击监听器的接口

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);

    }//item长点击监听器的接口
}
