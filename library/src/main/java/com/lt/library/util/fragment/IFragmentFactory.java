package com.lt.library.util.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface IFragmentFactory {

    Fragment createProduct(@NonNull String prodType);
}
