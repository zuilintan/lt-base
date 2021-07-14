package com.lt.person_baseutil.view.activity;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.lt.library.base.BaseActivity;
import com.lt.person_baseutil.databinding.ActivityMainBinding;
import com.lt.person_baseutil.view.adapter.TestFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final List<String> mFragmentTagList = new ArrayList<String>() {
        {
            add("frag01");
            add("frag02");
        }
    };

    @Override
    protected ActivityMainBinding bindView() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        super.initView();
        for (int i = 0; i < mFragmentTagList.size(); i++) {
            mViewBinding.tlMain.addTab(mViewBinding.tlMain.newTab(), i, false);
        }
        PagerAdapter pagerAdapter = new TestFragmentPagerAdapter(getSupportFragmentManager(),
                                                                 FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                                                 mFragmentTagList);
        mViewBinding.viewPager.setAdapter(pagerAdapter);
        mViewBinding.tlMain.setupWithViewPager(mViewBinding.viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewBinding.tlMain.getTabAt(0).select();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
