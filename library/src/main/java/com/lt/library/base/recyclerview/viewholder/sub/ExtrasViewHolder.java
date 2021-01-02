package com.lt.library.base.recyclerview.viewholder.sub;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnExtrasClickListener;
import com.lt.library.base.recyclerview.listener.OnExtrasLongClickListener;
import com.lt.library.base.recyclerview.viewholder.BaseViewHolder;

public class ExtrasViewHolder extends BaseViewHolder {

    public ExtrasViewHolder(@NonNull View itemView, OnExtrasClickListener onExtrasClickListener, OnExtrasLongClickListener onExtrasLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onExtrasClickListener != null) {
                onExtrasClickListener.onExtrasClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onExtrasLongClickListener != null) {
                b = onExtrasLongClickListener.onExtrasLongClick(view);
            }
            return b;
        });
    }
}
