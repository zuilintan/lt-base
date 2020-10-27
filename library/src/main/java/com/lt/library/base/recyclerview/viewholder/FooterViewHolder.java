package com.lt.library.base.recyclerview.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnFooterClickListener;
import com.lt.library.base.recyclerview.listener.OnFooterLongClickListener;

public class FooterViewHolder extends BaseViewHolder {

    public FooterViewHolder(@NonNull View itemView, OnFooterClickListener onFooterClickListener, OnFooterLongClickListener onFooterLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onFooterClickListener != null) {
                onFooterClickListener.onFooterClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onFooterLongClickListener != null) {
                b = onFooterLongClickListener.onFooterLongClick(view);
            }
            return b;
        });
    }
}
