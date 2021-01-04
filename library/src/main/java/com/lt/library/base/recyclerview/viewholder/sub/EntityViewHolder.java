package com.lt.library.base.recyclerview.viewholder.sub;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnEntityClickListener;
import com.lt.library.base.recyclerview.listener.OnEntityLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;
import com.lt.library.util.LogUtil;

public class EntityViewHolder extends BaseViewHolder {

    public EntityViewHolder(@NonNull View itemView, OnEntityClickListener onEntityClickListener, OnEntityLongClickListener onEntityLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onEntityClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return;
                }
                onEntityClickListener.onEntityClick(view, (int) itemView.getTag());
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
                b = onEntityLongClickListener.onEntityLongClick(view, (int) itemView.getTag());
            }
            return b;
        });
    }
}
