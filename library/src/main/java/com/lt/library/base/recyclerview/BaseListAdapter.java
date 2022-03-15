package com.lt.library.base.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

/**
 * @Auth: LinTan
 * @Date: 2021/11/19 15:24
 * @Desc: 封装业务无关的RecyclerView.Adapter基类
 */

public abstract class BaseListAdapter<DS, VH extends BaseViewHolder> extends ListAdapter<DS, VH> {
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseListAdapter(DiffUtil.ItemCallback<DS> itemCallback) {
        super(itemCallback);
    }

    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH viewHolder = onCreateItemViewHolder(parent.getContext(), parent, viewType, bindItemLayoutRes(viewType));
        viewHolder.setOnItemClickListener(mOnItemClickListener);
        viewHolder.setOnItemLongClickListener(mOnItemLongClickListener);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(@NonNull VH viewHolder, int position) {
        onBindItemViewHolder(viewHolder, getItem(position), position, getItemViewType(position));
    }

    @Override
    public final void onBindViewHolder(@NonNull VH viewHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position);
            return;
        }
        onBindItemViewHolder(viewHolder, getItem(position), position, getItemViewType(position), payloads);
    }

    @Override
    public final void onViewRecycled(@NonNull VH viewHolder) {
        super.onViewRecycled(viewHolder);
        onFreeItemViewHolder(viewHolder);
    }

    @Override
    public final int getItemViewType(int position) {
        return bindItemViewType(position);
    }

    protected int bindItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract int bindItemLayoutRes(int viewType);

    protected abstract VH onCreateItemViewHolder(Context context, ViewGroup viewGroup, int viewType, int layoutRes);

    protected abstract void onBindItemViewHolder(@NonNull VH viewHolder, @NonNull DS dataSource, int position, int viewType);

    protected void onBindItemViewHolder(@NonNull VH viewHolder, @NonNull DS dataSource, int position, int viewType, @NonNull List<Object> payloads) {
    }

    protected void onFreeItemViewHolder(@NonNull VH viewHolder) {
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
}
