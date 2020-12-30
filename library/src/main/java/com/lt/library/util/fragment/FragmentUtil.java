package com.lt.library.util.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.lt.library.util.LogUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @作者: LinTan
 * @日期: 2020/12/4 21:31
 * @版本: 1.0
 * @描述: //FragmentUtil
 * 1.0: Initial Commit
 */

public class FragmentUtil {
    private IFragmentFactory mFragmentFactory;

    private FragmentUtil() {
    }

    public static FragmentUtil getInstance() {
        return FragmentUtilHolder.INSTANCE;
    }

    public void switchFragment(String fragmentTag, int[] hideParentIds, Integer showParentId, FragmentManager supportFragmentManager) {
        switchFragment(fragmentTag, hideParentIds, showParentId, supportFragmentManager, FragmentTransaction.TRANSIT_NONE);
    }

    public void switchFragment(String fragmentTag, int[] hideParentIds, Integer showParentId, FragmentManager supportFragmentManager, int transitionEffect) {
        Fragment fragment = getFragment(fragmentTag, supportFragmentManager);
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        hideFragmentForParent(fragment, supportFragmentManager.getFragments(), IntStream.of(hideParentIds).boxed().collect(Collectors.toList()), fragmentTransaction);
        if (Objects.nonNull(fragment)) {
            showFragmentForTarget(fragment, fragmentTag, showParentId, fragmentTransaction);
        }
        fragmentTransaction.setTransition(transitionEffect)
                           .commit();
    }

    @Nullable
    private Fragment getFragment(String fragmentTag, FragmentManager supportFragmentManager) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTag);
        if (Objects.isNull(fragment)) {
            fragment = mFragmentFactory.createProduct(fragmentTag);
        }
        return fragment;
    }

    private void hideFragmentForParent(@Nullable Fragment fragment, List<Fragment> addedFragmentList, List<Integer> hideParentIdList, FragmentTransaction fragmentTransaction) {
        for (Fragment addedFragment : addedFragmentList) {
            if (Objects.equals(addedFragment, fragment)) {
                continue;
            }
            if (addedFragment.isHidden()) {
                continue;
            }
            ViewGroup viewGroup = (ViewGroup) addedFragment.getView().getParent();
            if (hideParentIdList.contains(viewGroup.getId())) {
                fragmentTransaction.hide(addedFragment);
                LogUtil.d(addedFragment + " will hide");
            }
        }
    }

    private void showFragmentForTarget(@NonNull Fragment fragment, String fragmentTag, Integer showParentId, FragmentTransaction fragmentTransaction) {
        if (!fragment.isAdded()) {
            fragmentTransaction.add(showParentId, fragment, fragmentTag);
            LogUtil.d(fragment + " will add");
        }//若未Add, 则Add
        else if (fragment.isHidden()) {
            fragmentTransaction.show(fragment);
            LogUtil.d(fragment + " will show");
        }//若已Add, 未Show, 则Show
    }

    public void setFragmentFactory(IFragmentFactory fragmentFactory) {
        mFragmentFactory = fragmentFactory;
    }

    private static class FragmentUtilHolder {
        private static final FragmentUtil INSTANCE = new FragmentUtil();
    }
}
