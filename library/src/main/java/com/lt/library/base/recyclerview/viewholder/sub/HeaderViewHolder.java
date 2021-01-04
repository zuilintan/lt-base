package com.lt.library.base.recyclerview.viewholder.sub;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnHeaderClickListener;
import com.lt.library.base.recyclerview.listener.OnHeaderLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;

public class HeaderViewHolder extends BaseViewHolder {

    public HeaderViewHolder(@NonNull View itemView, OnHeaderClickListener onHeaderClickListener, OnHeaderLongClickListener onHeaderLongClickListener) {
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
