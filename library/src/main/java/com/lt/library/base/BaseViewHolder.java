package com.lt.library.base;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.library.util.context.ContextUtil;

import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2018/12/12 12:10
 * @版本: 1.0
 * @描述: //BaseViewHolder, 注意引入依赖
 * 源址: https://blog.csdn.net/a_zhon/article/details/66971369
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mSparseArray;//Key为View的Id，Value为View对象

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mSparseArray = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int viewId) {
        View view = mSparseArray.get(viewId);
        if (Objects.isNull(view)) {
            view = itemView.findViewById(viewId);
            mSparseArray.put(viewId, view);
        }
        return (T) view;
    }//findViewById，并复用

    public void setText(int viewId, CharSequence text) {
        TextView view = findViewById(viewId);
        view.setText(text);
    }//设置文本

    public void setText(int viewId, @StringRes int stringId) {
        TextView view = findViewById(viewId);
        view.setText(stringId);
    }//设置文本

    public String getText(int viewId) {
        TextView view = findViewById(viewId);
        return view.getText().toString();
    }//获取文本

    public void setTextSize(int viewId, float size) {
        TextView view = findViewById(viewId);
        view.setTextSize(size);
    }//设置文本大小

    public void setTextColor(int viewId, @ColorRes int resId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(ContextUtil.getInstance().getContext(), resId));
    }//设置文本颜色

    public void setTextColorStateList(int viewId, @ColorRes int resId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColorStateList(ContextUtil.getInstance().getContext(), resId));
    }//设置文本颜色

    public void setImageDrawable(int viewId, @Nullable Drawable drawable) {
        ImageView view = findViewById(viewId);
        view.setImageDrawable(drawable);
    }//设置图片资源

    public void setImageResource(int viewId, @DrawableRes int resId) {
        ImageView view = findViewById(viewId);
        view.setImageResource(resId);
    }//设置图片资源

    public void setBackground(int viewId, @Nullable Drawable drawable) {
        View view = findViewById(viewId);
        view.setBackground(drawable);
    }//设置背景资源

    public void setBackgroundResource(int viewId, @DrawableRes int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
    }//设置背景资源

    public void setBackgroundColor(int viewId, @ColorRes int resId) {
        View view = findViewById(viewId);
        view.setBackgroundColor(ContextCompat.getColor(ContextUtil.getInstance().getContext(), resId));
    }//设置背景颜色

    public void setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        view.setVisibility(visibility);
    }//设置显隐

    public void setOnClickListener(int viewId, @Nullable View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
    }//设置点击事件
}
