package com.lt.library.base.recyclerview.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.lt.library.base.recyclerview.listener.OnStatusClickListener;
import com.lt.library.base.recyclerview.listener.OnStatusLongClickListener;

public class StatusViewHolder extends BaseViewHolder {

    public StatusViewHolder(@NonNull View itemView, OnStatusClickListener onStatusClickListener, OnStatusLongClickListener onStatusLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (onStatusClickListener != null) {
                onStatusClickListener.onStatusClick(view);
            }
        });
        itemView.setOnLongClickListener(view -> {
            boolean b = false;
            if (onStatusLongClickListener != null) {
                b = onStatusLongClickListener.onStatusLongClick(view);
            }
            return b;
        });
    }
}
