package com.lt.library.base.recyclerview.holder.sub;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.holder.BaseViewHolder;
import com.lt.library.base.recyclerview.listener.OnExtrasItemClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasItemLongClickListener;

public class ExtrasViewHolder extends BaseViewHolder {

    public ExtrasViewHolder(@NonNull View itemView, OnExtrasItemClickListener onExtrasItemClickListener, OnExtrasItemLongClickListener onExtrasItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onExtrasItemClickListener != null) {
                onExtrasItemClickListener.onExtrasClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onExtrasItemLongClickListener != null) {
                b = onExtrasItemLongClickListener.onExtrasLongClick(view);
            }
            return b;
        });
    }
}
