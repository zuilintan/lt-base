package com.lt.person_baseutil.view.state;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mFillLiveData = new MutableLiveData<>();

    public void requestFill(boolean enable) {
        mFillLiveData.setValue(enable);
    }

    public LiveData<Boolean> getFillLiveData() {
        return mFillLiveData;
    }
}
