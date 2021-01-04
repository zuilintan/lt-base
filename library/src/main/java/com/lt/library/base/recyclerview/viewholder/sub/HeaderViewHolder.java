package com.lt.library.base.recyclerview.viewholder.sub;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnHeaderItemClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderItemLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;

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
