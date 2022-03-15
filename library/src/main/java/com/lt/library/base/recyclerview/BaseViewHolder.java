package com.lt.library.base.recyclerview;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lt.library.util.CastUtil;
import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;

/**
 * @Auth: LinTan
 * @Date: 2021/11/19 15:24
 * @Desc: 封装业务无关的ViewHolder基类
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViewArray;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    {
        mViewArray = new SparseArray<>();
    }

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return;
                }
                mOnItemClickListener.onClick(v, position);
            }
        });
        itemView.setOnLongClickListener(v -> {
            boolean b = false;
            if (mOnItemLongClickListener != null) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    LogUtil.w("view is changing");
                    return false;
                }
                b = mOnItemLongClickListener.onLongClick(v, position);
            }
            return b;
        });
    }

    public final <T extends View> T getView(int viewId) {
        return getView(viewId, null);
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T getView(int viewId, Class<T> cls) {
        View view = mViewArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViewArray.put(viewId, view);
        }
        T t;
        if (cls == null) {
            t = (T) view;
        } else {
            t = CastUtil.obj2Obj(view, cls);
        }
        return t;
    }

    protected final Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
}
