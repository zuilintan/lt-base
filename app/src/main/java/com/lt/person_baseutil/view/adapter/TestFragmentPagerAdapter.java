package com.lt.person_baseutil.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lt.person_baseutil.view.fragment.Test01Fragment;
import com.lt.person_baseutil.view.fragment.Test02Fragment;

import java.util.List;

public class TestFragmentPagerAdapter extends FragmentStateAdapter {
    private final FragmentManager mFragmentManager;
    private final List<String> mFragmentTagList;

    public TestFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<String> fragmentTagList) {
        super(fragmentManager, lifecycle);
        mFragmentManager = fragmentManager;
        mFragmentTagList = fragmentTagList;
    }

    /*
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }*/

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return getFragment(mFragmentTagList.get(position), mFragmentManager);
    }

    @Override
    public int getItemCount() {
        return mFragmentTagList.size();
    }

    private Fragment getFragment(String fragmentTag, FragmentManager fragmentManager) {
        /*Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = createFragment(fragmentTag);
        }*/
        return createFragment(fragmentTag);
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
