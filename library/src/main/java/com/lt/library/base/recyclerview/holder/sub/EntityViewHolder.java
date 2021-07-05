package com.lt.library.base.recyclerview.holder.sub;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.listener.OnEntityItemClickListener;
import com.lt.library.base.recyclerview.listener.OnEntityItemLongClickListener;
import com.lt.library.util.LogUtil;

public class EntityViewHolder extends BaseViewHolder {

    public EntityViewHolder(@NonNull View itemView, OnEntityItemClickListener onEntityItemClickListener, OnEntityItemLongClickListener onEntityItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onEntityItemClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return;
                }
                onEntityItemClickListener.onEntityClick(view, (int) itemView.getTag());
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onEntityItemLongClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return false;
                }
                b = onEntityItemLongClickListener.onEntityLongClick(view, (int) itemView.getTag());
            }
            return b;
        });
    }
}
