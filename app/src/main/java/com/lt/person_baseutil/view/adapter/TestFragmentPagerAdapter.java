package com.lt.person_baseutil.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lt.person_baseutil.view.fragment.Test01Fragment;
import com.lt.person_baseutil.view.fragment.Test02Fragment;

import java.util.List;

public class TestFragmentPagerAdapter extends FragmentStateAdapter {
    private final List<String> mFragmentTagList;

    public TestFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> fragmentTagList) {
        super(fragmentActivity);
        mFragmentTagList = fragmentTagList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        String fragmentTag = mFragmentTagList.get(position);
        switch (mFragmentTagList.get(position)) {
            case "frag01":
                fragment = new Test01Fragment();
                break;
            case "frag02":
                fragment = new Test02Fragment();
                break;
            default:
                throw new IllegalStateException("unexpected value: " + fragmentTag);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mFragmentTagList.size();
    }
}
