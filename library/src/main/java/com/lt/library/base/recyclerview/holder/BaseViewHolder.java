package com.lt.library.base.recyclerview.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

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
    private final SparseArray<View> mSparseArray;//Key为View的Id，Value为View对象

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mSparseArray = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mSparseArray.put(viewId, view);
        }
        return (T) view;
    }//findViewById，并复用

    public Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    public BaseViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }//设置文本

    public BaseViewHolder setText(int viewId, @StringRes int stringId) {
        TextView view = getView(viewId);
        view.setText(stringId);
        return this;
    }//设置文本

    public String getText(int viewId) {
        TextView view = getView(viewId);
        return view.getText().toString();
    }//获取文本

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId) {
        setTextSize(viewId, dimenId, TypedValue.COMPLEX_UNIT_SP);
        return this;
    }//设置文本大小

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId, int unit) {
        TextView view = getView(viewId);
        view.setTextSize(unit, getAppContext().getResources().getDimension(dimenId));
        return this;
    }//设置文本大小

    public BaseViewHolder setTextColor(int viewId, @ColorRes int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColor(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setTextColorStateList(int viewId, @ColorRes int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColorStateList(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setImageDrawable(int viewId, @DrawableRes int drawableId) {
        ImageView view = getView(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setImageDrawable(null);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(getAppContext(), drawableId));
        }
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTint(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = getView(viewId);
        Drawable drawable = ContextCompat.getDrawable(getAppContext(), drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(getAppContext(), colorId));
        view.setImageDrawable(drawable);
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTintList(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = getView(viewId);
        Drawable drawable = ContextCompat.getDrawable(getAppContext(), drawableId);
        DrawableCompat.setTintList(Objects.requireNonNull(drawable), ContextCompat.getColorStateList(getAppContext(), colorId));
        view.setImageDrawable(drawable);
        return this;
    }//设置图片资源

    public BaseViewHolder setBackground(int viewId, @DrawableRes int drawableId) {
        View view = getView(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setBackground(null);
        } else {
            view.setBackground(ContextCompat.getDrawable(getAppContext(), drawableId));
        }
        return this;
    }//设置背景资源

    public BaseViewHolder setBackgroundColor(int viewId, @ColorRes int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(ContextCompat.getColor(getAppContext(), colorId));
        return this;
    }//设置背景颜色

    public BaseViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }//设置显隐状态

    public BaseViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }//设置选中状态

    public BaseViewHolder setOnClickListener(int viewId, @Nullable View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }//设置点击事件
}
