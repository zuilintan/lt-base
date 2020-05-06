package com.lt.library.util.event;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者: LinTan
 * @日期: 2020/5/6 23:32
 * @版本: 1.0
 * @描述: //LiveDataBusUtil
 * 源址: https://github.com/BugRui/LiveDataBus
 * 1.0: Initial Commit
 */

public class LiveDataBusUtil {
    private ConcurrentHashMap<String, MutableLiveData<Object>> mLiveDataMap;

    private LiveDataBusUtil() {
        mLiveDataMap = new ConcurrentHashMap<>();
    }

    public static LiveDataBusUtil getInstance() {
        return LiveDataBusHolder.INSTANCE;
    }

    public LiveData<Object> with(int tag) {
        return with(String.valueOf(tag));
    }

    public LiveData<Object> with(String tag) {
        MutableLiveData<Object> mutableLiveData = new MutableLiveData<>();
        mLiveDataMap.put(tag, mutableLiveData);
        return mutableLiveData;
    }

    public LiveDataSticky withStick(int tag) {
        return withStick(String.valueOf(tag));
    }

    public LiveDataSticky withStick(String tag) {
        return new LiveDataSticky(tag, mLiveDataMap);
    }

    public <T> void send(int tag, T t) {
        send(String.valueOf(tag), t);
    }

    public <T> void send(String tag, T t) {
        if (!mLiveDataMap.containsKey(tag) || Objects.isNull(mLiveDataMap.get(tag))) {
            return;
        }
        Objects.requireNonNull(mLiveDataMap.get(tag)).setValue(t);
    }

    public <T> void sendPost(int tag, T t) {
        sendPost(String.valueOf(tag), t);
    }

    public <T> void sendPost(String tag, T t) {
        if (!mLiveDataMap.containsKey(tag) || Objects.isNull(mLiveDataMap.get(tag))) {
            return;
        }
        Objects.requireNonNull(mLiveDataMap.get(tag)).postValue(t);
    }

    public <T> void sendStick(int tag, T t) {
        sendStick(String.valueOf(tag), t);
    }

    public <T> void sendStick(String tag, T t) {
        if (!mLiveDataMap.containsKey(tag) || Objects.isNull(mLiveDataMap.get(tag))) {
            mLiveDataMap.put(tag, new MutableLiveData<>());
        }
        Objects.requireNonNull(mLiveDataMap.get(tag)).setValue(t);
    }

    public <T> void sendStickPost(int tag, T t) {
        sendStickPost(String.valueOf(tag), t);
    }

    public <T> void sendStickPost(String tag, T t) {
        if (!mLiveDataMap.containsKey(tag) || Objects.isNull(mLiveDataMap.get(tag))) {
            mLiveDataMap.put(tag, new MutableLiveData<>());
        }
        Objects.requireNonNull(mLiveDataMap.get(tag)).postValue(t);
    }

    private static class LiveDataBusHolder {
        private static final LiveDataBusUtil INSTANCE = new LiveDataBusUtil();
    }
}
