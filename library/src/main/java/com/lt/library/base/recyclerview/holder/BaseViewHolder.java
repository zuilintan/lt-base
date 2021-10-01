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
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lt.library.util.CastUtil;
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

    public <T extends View> T getView(int viewId) {
        return getView(viewId, null);
    }//findViewById，并复用

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId, Class<T> cls) {
        View view = mSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mSparseArray.put(viewId, view);
        }
        T t;
        if (cls == null) {
            t = (T) view;
        } else {
            t = CastUtil.obj2Obj(view, cls);
        }
        return t;
    }//findViewById，并复用

    public Context getAppContext() {
        return ContextUtil.getAppContext();
    }

    public BaseViewHolder setText(int viewId, CharSequence text) {
        getView(viewId, TextView.class).setText(text);
        return this;
    }//设置文本

    public BaseViewHolder setText(int viewId, @StringRes int stringId) {
        getView(viewId, TextView.class).setText(stringId);
        return this;
    }//设置文本

    public String getText(int viewId) {
        return getView(viewId, TextView.class).getText().toString();
    }//获取文本

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId) {
        return setTextSize(viewId, dimenId, TypedValue.COMPLEX_UNIT_SP);
    }//设置文本大小

    public BaseViewHolder setTextSize(int viewId, @DimenRes int dimenId, int unit) {
        getView(viewId, TextView.class).setTextSize(unit, getAppContext().getResources().getDimension(dimenId));
        return this;
    }//设置文本大小

    public BaseViewHolder setTextColor(int viewId, @ColorRes int colorId) {
        getView(viewId, TextView.class).setTextColor(ContextCompat.getColor(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setTextColorStateList(int viewId, @ColorRes int colorId) {
        getView(viewId, TextView.class).setTextColor(ContextCompat.getColorStateList(getAppContext(), colorId));
        return this;
    }//设置文本颜色

    public BaseViewHolder setImageDrawable(int viewId, @DrawableRes int drawableId) {
        ImageView view = getView(viewId, ImageView.class);
        if (drawableId == RES_EMPTY_ID) {
            view.setImageDrawable(null);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(getAppContext(), drawableId));
        }
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTint(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = getView(viewId, ImageView.class);
        Drawable drawable = ContextCompat.getDrawable(getAppContext(), drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(getAppContext(), colorId));
        view.setImageDrawable(drawable);
        return this;
    }//设置图片资源

    public BaseViewHolder setImageDrawableTintList(int viewId, @DrawableRes int drawableId, @ColorRes int colorId) {
        ImageView view = getView(viewId, ImageView.class);
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
}
