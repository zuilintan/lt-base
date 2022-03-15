package com.lt.library.util;

import android.app.Activity;

import java.util.Stack;

/**
 * @作者: LinTan
 * @日期: 2018/12/29 19:53
 * @版本: 1.0
 * @描述: ActivityUtil
 * 源址: 《第一行代码(第二版)》中 2.6 活动的最佳实践内的Example
 * 1.0: Initial Commit
 */

public class ActivityUtil {
    private static final Stack<Activity> sActivityStack = new Stack<>();

    private ActivityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void add(Activity activity) {
        sActivityStack.add(activity);
    }

    public static void remove(Activity activity) {
        sActivityStack.remove(activity);
    }

    public static Activity getFirst() {
        return sActivityStack.firstElement();
    }

    public static Activity getLast() {
        return sActivityStack.lastElement();
    }

    public static void finish(Activity activity) {
        sActivityStack.remove(activity);
        activity.finish();
    }

    public static void finishAll() {
        for (Activity activity : sActivityStack) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        sActivityStack.clear();
    }
}
