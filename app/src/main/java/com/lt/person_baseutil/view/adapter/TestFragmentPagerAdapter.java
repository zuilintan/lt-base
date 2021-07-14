package com.lt.person_baseutil.view.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lt.person_baseutil.view.fragment.Test01Fragment;
import com.lt.person_baseutil.view.fragment.Test02Fragment;

import java.util.List;

public class TestFragmentPagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager mFragmentManager;
    private final List<String> mFragmentTagList;

    public TestFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, List<String> fragmentTagList) {
        super(fm, behavior);
        mFragmentManager = fm;
        mFragmentTagList = fragmentTagList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return getFragment(mFragmentTagList.get(position), mFragmentManager);
    }

    @Override
    public int getCount() {
        return mFragmentTagList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    private Fragment getFragment(String fragmentTag, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = createFragment(fragmentTag);
        }
        return fragment;
    }

    public Fragment createFragment(@NonNull String fragmentTag) {
        Fragment fragment;
        switch (fragmentTag) {
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
}
