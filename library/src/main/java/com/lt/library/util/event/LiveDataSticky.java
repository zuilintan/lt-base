package com.lt.library.util.event;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:59
 * @版本: 1.0
 * @描述: //LiveDataSticky
 * 1.0: Initial Commit
 */
public class LiveDataSticky {
    private final ConcurrentHashMap<String, MutableLiveData<Object>> mLiveDataMap;
    private final String mTag;

    public LiveDataSticky(String tag, ConcurrentHashMap<String, MutableLiveData<Object>> liveDataMap) {
        mTag = tag;
        mLiveDataMap = liveDataMap;
    }

    public void observe(LifecycleOwner owner, Observer<Object> observer) {
        if (!mLiveDataMap.containsKey(mTag) || Objects.isNull(mLiveDataMap.get(mTag))) {
            mLiveDataMap.put(mTag, new MutableLiveData<>());
        }
        Objects.requireNonNull(mLiveDataMap.get(mTag)).observe(owner, o -> {
            observer.onChanged(o);
            mLiveDataMap.put(mTag, new MutableLiveData<>());
            observe(owner, observer);
        });
    }
}
