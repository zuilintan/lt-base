package com.lt.library.base.recyclerview.viewholder.sub;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnFooterItemClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterItemLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;

public class FooterViewHolder extends BaseViewHolder {

    public FooterViewHolder(@NonNull View itemView, OnFooterItemClickListener onFooterItemClickListener, OnFooterItemLongClickListener onFooterItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onFooterItemClickListener != null) {
                onFooterItemClickListener.onFooterClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onFooterItemLongClickListener != null) {
                b = onFooterItemLongClickListener.onFooterLongClick(view);
            }
            return b;
        });
    }
}
