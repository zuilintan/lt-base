package com.lt.library.util.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者: LinTan
 * @日期: 2020/5/6 23:32
 * @版本: 1.0
 * @描述: LiveDataBusUtil
 * 源址: https://github.com/BugRui/LiveDataBus
 * 1.0: Initial Commit
 */

public class LiveDataBusUtil {
    private final ConcurrentHashMap<String, MutableLiveData<Object>> mLiveDataMap;

    private LiveDataBusUtil() {
        mLiveDataMap = new ConcurrentHashMap<>();
    }

    private static LiveDataBusUtil getInstance() {
        return LiveDataBusHolder.INSTANCE;
    }

    public static LiveData<Object> with(int tag) {
        return with(String.valueOf(tag));
    }

    public static LiveData<Object> with(String tag) {
        MutableLiveData<Object> mutableLiveData = new MutableLiveData<>();
        getInstance().mLiveDataMap.put(tag, mutableLiveData);
        return mutableLiveData;
    }

    public static LiveDataSticky withStick(int tag) {
        return withStick(String.valueOf(tag));
    }

    public static LiveDataSticky withStick(String tag) {
        return new LiveDataSticky(tag, getInstance().mLiveDataMap);
    }

    public static <T> void send(int tag, T t) {
        send(String.valueOf(tag), t);
    }

    public static <T> void send(String tag, T t) {
        if (!getInstance().mLiveDataMap.containsKey(tag) || getInstance().mLiveDataMap.get(tag) == null) {
            return;
        }
        Objects.requireNonNull(getInstance().mLiveDataMap.get(tag)).setValue(t);
    }

    public static <T> void sendPost(int tag, T t) {
        sendPost(String.valueOf(tag), t);
    }

    public static <T> void sendPost(String tag, T t) {
        if (!getInstance().mLiveDataMap.containsKey(tag) || getInstance().mLiveDataMap.get(tag) == null) {
            return;
        }
        Objects.requireNonNull(getInstance().mLiveDataMap.get(tag)).postValue(t);
    }

    public static <T> void sendStick(int tag, T t) {
        sendStick(String.valueOf(tag), t);
    }

    public static <T> void sendStick(String tag, T t) {
        if (!getInstance().mLiveDataMap.containsKey(tag) || getInstance().mLiveDataMap.get(tag) == null) {
            getInstance().mLiveDataMap.put(tag, new MutableLiveData<>());
        }
        Objects.requireNonNull(getInstance().mLiveDataMap.get(tag)).setValue(t);
    }

    public static <T> void sendStickPost(int tag, T t) {
        sendStickPost(String.valueOf(tag), t);
    }

    public static <T> void sendStickPost(String tag, T t) {
        if (!getInstance().mLiveDataMap.containsKey(tag) || getInstance().mLiveDataMap.get(tag) == null) {
            getInstance().mLiveDataMap.put(tag, new MutableLiveData<>());
        }
        Objects.requireNonNull(getInstance().mLiveDataMap.get(tag)).postValue(t);
    }

    private static class LiveDataBusHolder {
        private static final LiveDataBusUtil INSTANCE = new LiveDataBusUtil();
    }
}
