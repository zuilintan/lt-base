package com.lt.library.util;

import android.app.Activity;
import android.view.View;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:57
 * @版本: 1.0
 * @描述: SystemUiUtil
 * 1.0: Initial Commit
 */

public class SystemUiUtil {

    private SystemUiUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 更改SystemUI的可见性
     *
     * @param activity     Activity
     * @param visibilities View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//稳定布局, View内容不会随StatusBar与NavigationBar显隐而移动
     *                     View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;//不隐藏StatusBar, 将Activity扩展至StatusBar底部
     *                     View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;//不隐藏NavigationBar, 将Activity扩展至NavigationBar底部
     *                     View.SYSTEM_UI_FLAG_FULLSCREEN;//隐藏StatusBar, 将windowBackground扩展至StatusBar位置
     *                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;//隐藏NavigationBar, 将windowBackground扩展至NavigationBar位置
     *                     View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;//避免UI交互造成系统清除全屏状态, 但边缘处可以滑出StatusBar与NavigationBar
     */
    public static void setVisibility(Activity activity, int... visibilities) {
        int visibility = View.SYSTEM_UI_FLAG_VISIBLE;
        for (int i : visibilities) {
            visibility |= i;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
    }
}
