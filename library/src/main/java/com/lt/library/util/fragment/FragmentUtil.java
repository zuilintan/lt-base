package com.lt.library.util.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.lt.library.util.LogUtil;

import java.util.Arrays;
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

    private static FragmentUtil getInstance() {
        return FragmentUtilHolder.INSTANCE;
    }

    @Nullable
    private static Fragment getFragment(String fragmentTag, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (Objects.isNull(fragment)) {
            fragment = getInstance().mFragmentFactory.createProduct(fragmentTag);
        }
        return fragment;
    }

    private static void setFragmentAnimations(@NonNull int[] animations, FragmentTransaction fragmentTransaction) {
        if (Objects.isNull(animations)) {
            return;
        }
        if (animations.length == 2) {
            fragmentTransaction.setCustomAnimations(animations[0], animations[1]);
        } else if (animations.length == 4) {
            fragmentTransaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
        } else {
            throw new IllegalArgumentException("unexpected animations = " + Arrays.toString(animations));
        }
    }

    private static void hideFragmentForParent(@Nullable Fragment fragment, List<Fragment> addedFragmentList, List<Integer> hideParentIdList, FragmentTransaction fragmentTransaction) {
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

    private static void showFragmentForTarget(@NonNull Fragment fragment, String fragmentTag, Integer showParentId, FragmentTransaction fragmentTransaction) {
        if (!fragment.isAdded()) {
            fragmentTransaction.add(showParentId, fragment, fragmentTag);
            LogUtil.d(fragment + " will add");
        }//若未Add, 则Add
        else if (fragment.isHidden()) {
            fragmentTransaction.show(fragment);
            LogUtil.d(fragment + " will show");
        }//若已Add, 未Show, 则Show
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     准备显示的Fragment的tag
     * @param hideHostIds     准备隐藏的Fragment所在容器Id(可能有多个)
     * @param showHostId      准备显示的Fragment所在容器Id(仅支持一个)
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, int[] hideHostIds, Integer showHostId, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, hideHostIds, showHostId, null, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     准备显示的Fragment的tag
     * @param hideHostIds     准备隐藏的Fragment所在容器Id(可能有多个)
     * @param showHostId      准备显示的Fragment所在容器Id(仅支持一个)
     * @param animations      Fragment hide and show 动画{@link FragmentTransaction#setCustomAnimations}
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, int[] hideHostIds, Integer showHostId, int[] animations, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, hideHostIds, showHostId, animations, animations, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     准备显示的Fragment的tag
     * @param hideHostIds     准备隐藏的Fragment所在容器Id(可能有多个)
     * @param showHostId      准备显示的Fragment所在容器Id(仅支持一个)
     * @param hideAnimations  Fragment hide 动画{@link FragmentTransaction#setCustomAnimations}
     * @param showAnimations  Fragment show 动画{@link FragmentTransaction#setCustomAnimations}
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, int[] hideHostIds, Integer showHostId, int[] hideAnimations, int[] showAnimations, FragmentManager fragmentManager) {
        Fragment fragment = getFragment(fragmentTag, fragmentManager);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        setFragmentAnimations(hideAnimations, fragmentTransaction);
        hideFragmentForParent(fragment, fragmentManager.getFragments(), IntStream.of(hideHostIds).boxed().collect(Collectors.toList()), fragmentTransaction);
        if (Objects.nonNull(fragment)) {
            setFragmentAnimations(showAnimations, fragmentTransaction);
            showFragmentForTarget(fragment, fragmentTag, showHostId, fragmentTransaction);
        }
        fragmentTransaction.commit();
    }

    public static void setFragmentFactory(IFragmentFactory fragmentFactory) {
        getInstance().mFragmentFactory = fragmentFactory;
    }

    private static class FragmentUtilHolder {
        private static final FragmentUtil INSTANCE = new FragmentUtil();
    }
}
