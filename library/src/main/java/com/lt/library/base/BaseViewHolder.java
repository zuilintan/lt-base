package com.lt.library.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
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
    public static final int RES_EMPTY_ID = 0;
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

    public Context getContext() {
        return ContextUtil.getInstance().getContext();
    }

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

    public void setTextSize(int viewId, @DimenRes int dimenId) {
        setTextSize(viewId, dimenId, TypedValue.COMPLEX_UNIT_SP);
    }//设置文本大小

    public void setTextSize(int viewId, @DimenRes int dimenId, int unit) {
        TextView view = findViewById(viewId);
        view.setTextSize(unit, getContext().getResources().getDimension(dimenId));
    }//设置文本大小

    public void setTextColor(int viewId, @ColorRes int colorId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }//设置文本颜色

    public void setTextColorStateList(int viewId, @ColorRes int colorId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColorStateList(getContext(), colorId));
    }//设置文本颜色

    public void setImageDrawable(int viewId, @DrawableRes int drawableId) {
        ImageView view = findViewById(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setImageDrawable(null);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
        }
    }//设置图片资源

    public void setImageDrawableTint(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = findViewById(viewId);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(getContext(), colorId));
        view.setImageDrawable(drawable);
    }//设置图片资源

    public void setImageDrawableTintList(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = findViewById(viewId);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        DrawableCompat.setTintList(Objects.requireNonNull(drawable), ContextCompat.getColorStateList(getContext(), colorId));
        view.setImageDrawable(drawable);
    }//设置图片资源

    public void setBackground(int viewId, @DrawableRes int drawableId) {
        View view = findViewById(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setBackground(null);
        } else {
            view.setBackground(ContextCompat.getDrawable(getContext(), drawableId));
        }
    }//设置背景资源

    public void setBackgroundColor(int viewId, @ColorRes int colorId) {
        View view = findViewById(viewId);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));
    }//设置背景颜色

    public void setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        view.setVisibility(visibility);
    }//设置显隐状态

    public void setOnClickListener(int viewId, @Nullable View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
    }//设置点击事件
}
