package com.lt.library.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @作者: LinTan
 * @日期: 2020/1/13 15:50
 * @版本: 1.0
 * @描述: //ScrollBarUtil, 通过反射动态设置RecyclerView或ListView的滚动条的显示效果
 * 1.0: Initial Commit
 */

public class ScrollBarUtil {
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    private ScrollBarUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setThumb(View view, @Orientation int orientation, Drawable drawable) {
        Object scrollCacheObj = ReflectionUtil.invokeMethod(View.class, view, "getScrollCache");
        Object scrollBarObj = ReflectionUtil.getField(scrollCacheObj, "scrollBar");
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                ReflectionUtil.setField(scrollBarObj, "mHorizontalThumb", drawable);
                break;
            case ORIENTATION_VERTICAL:
                ReflectionUtil.setField(scrollBarObj, "mVerticalThumb", drawable);
                break;
            default:
                break;
        }
    }

    @IntDef({ORIENTATION_HORIZONTAL, ORIENTATION_VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }
}
