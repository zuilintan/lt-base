package com.lt.library.util.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface IFragmentFactory {
    Fragment createProduct(@NonNull String type, @Nullable String param1, @Nullable String param2);
}
