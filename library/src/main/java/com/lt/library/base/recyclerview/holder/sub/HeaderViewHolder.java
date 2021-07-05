package com.lt.library.base.recyclerview.holder.sub;

import android.view.View;

import androidx.annotation.NonNull;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.listener.OnHeaderItemClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderItemLongClickListener;

public class HeaderViewHolder extends BaseViewHolder {

    public HeaderViewHolder(@NonNull View itemView, OnHeaderItemClickListener onHeaderItemClickListener, OnHeaderItemLongClickListener onHeaderItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onHeaderItemClickListener != null) {
                onHeaderItemClickListener.onHeaderClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onHeaderItemLongClickListener != null) {
                b = onHeaderItemLongClickListener.onHeaderLongClick(view);
            }
            return b;
        });
    }
}
