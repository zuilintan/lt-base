package com.lt.library.util;

import android.util.TypedValue;

import com.lt.library.util.context.ContextUtil;

/**
 * @作者: LinTan
 * @日期: 2019/1/6 11:12
 * @版本: 1.0
 * @描述: //DensityUtil。
 * 源址: https://blog.csdn.net/lmj623565791/article/details/38965311
 * 1.0: Initial Commit
 */

public class DensityUtil {
    private DensityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int dp2px(float dpValue) {
        float density = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                                                  ContextUtil.getInstance().getContext()
                                                             .getResources()
                                                             .getDisplayMetrics());
        return (int) (density + 0.5F);//加0.5F以四舍五入，eg: 1.4+0.5=1.9转为int是1，而1.5 + 0.5 = 2.0转换成int后就是2
    }//dp转px

    public static int px2dp(float pxValue) {
        float scale = ContextUtil.getInstance().getContext()
                                 .getResources()
                                 .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);//加0.5F以四舍五入，eg: 1.4+0.5=1.9转为int是1，而1.5 + 0.5 = 2.0转换成int后就是2
    }//px转dp

    public static int sp2px(float spValue) {
        float density = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                                                  ContextUtil.getInstance().getContext()
                                                             .getResources()
                                                             .getDisplayMetrics());
        return (int) (density + 0.5F);//加0.5F以四舍五入，eg: 1.4+0.5=1.9转为int是1，而1.5 + 0.5 = 2.0转换成int后就是2
    }//sp转px

    public static int px2sp(float pxValue) {
        float scale = ContextUtil.getInstance().getContext()
                                 .getResources()
                                 .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5F);//加0.5F以四舍五入，eg: 1.4+0.5=1.9转为int是1，而1.5 + 0.5 = 2.0转换成int后就是2
    }//px转sp
}