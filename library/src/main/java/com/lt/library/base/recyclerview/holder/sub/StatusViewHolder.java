package com.lt.library.base.recyclerview.holder.sub;

import android.view.View;

import androidx.annotation.NonNull;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.listener.OnStatusItemClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusItemLongClickListener;

public class StatusViewHolder extends BaseViewHolder {

    public StatusViewHolder(@NonNull View itemView, OnStatusItemClickListener onStatusItemClickListener, OnStatusItemLongClickListener onStatusItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onStatusItemClickListener != null) {
                onStatusItemClickListener.onStatusClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onStatusItemLongClickListener != null) {
                b = onStatusItemLongClickListener.onStatusLongClick(view);
            }
            return b;
        });
    }
}
