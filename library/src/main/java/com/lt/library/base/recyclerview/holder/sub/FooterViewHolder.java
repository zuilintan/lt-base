package com.lt.library.base.recyclerview.holder.sub;

import android.view.View;

import androidx.annotation.NonNull;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.listener.OnFooterItemClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterItemLongClickListener;

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
