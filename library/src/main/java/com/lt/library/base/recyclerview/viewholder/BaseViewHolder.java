package com.lt.library.base.recyclerview.viewholder;

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

    public Context getAppContext() {
        return ContextUtil.getInstance().getApplication();
    }

    public BaseViewHolder setText(int viewId, CharSequence text) {
        TextView view = findViewById(viewId);
        view.setText(text);
        return this;
    }//设置文本

    public BaseViewHolder setText(int viewId, @StringRes int stringId) {
        TextView view = findViewById(viewId);
        view.setText(stringId);
        return this;
    }//设置文本

    public String getText(int viewId) {
        TextView view = findViewById(viewId);
        return view.getText().toString();
    }//获取文本

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId) {
        setTextSize(viewId, dimenId, TypedValue.COMPLEX_UNIT_SP);
        return this;
    }//设置文本大小

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId, int unit) {
        TextView view = findViewById(viewId);
        view.setTextSize(unit, getAppContext().getResources().getDimension(dimenId));
        return this;
    }//设置文本大小

    public BaseViewHolder setTextColor(int viewId, @ColorRes int colorId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setTextColorStateList(int viewId, @ColorRes int colorId) {
        TextView view = findViewById(viewId);
        view.setTextColor(ContextCompat.getColorStateList(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setImageDrawable(int viewId, @DrawableRes int drawableId) {
        ImageView view = findViewById(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setImageDrawable(null);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(getAppContext(), drawableId));
        }
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTint(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = findViewById(viewId);
        Drawable drawable = ContextCompat.getDrawable(getAppContext(), drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(getAppContext(), colorId));
        view.setImageDrawable(drawable);
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTintList(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = findViewById(viewId);
        Drawable drawable = ContextCompat.getDrawable(getAppContext(), drawableId);
        DrawableCompat.setTintList(Objects.requireNonNull(drawable), ContextCompat.getColorStateList(getAppContext(), colorId));
        view.setImageDrawable(drawable);
        return this;
    }//设置图片资源

    public BaseViewHolder setBackground(int viewId, @DrawableRes int drawableId) {
        View view = findViewById(viewId);
        if (drawableId == RES_EMPTY_ID) {
            view.setBackground(null);
        } else {
            view.setBackground(ContextCompat.getDrawable(getAppContext(), drawableId));
        }
        return this;
    }//设置背景资源

    public BaseViewHolder setBackgroundColor(int viewId, @ColorRes int colorId) {
        View view = findViewById(viewId);
        view.setBackgroundColor(ContextCompat.getColor(getAppContext(), colorId));
        return this;
    }//设置背景颜色

    public BaseViewHolder setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        view.setVisibility(visibility);
        return this;
    }//设置显隐状态

    public BaseViewHolder setOnClickListener(int viewId, @Nullable View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }//设置点击事件
}
