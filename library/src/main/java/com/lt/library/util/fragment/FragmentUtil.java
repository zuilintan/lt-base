package com.lt.library.util.fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private static Fragment getFragment(String fragmentTag, String param1, String param2, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = getInstance().mFragmentFactory.createProduct(fragmentTag, param1, param2);
        }
        return fragment;
    }

    private static void setFragmentAnimations(int[] animations, FragmentTransaction fragmentTransaction) {
        if (animations == null) {
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
                LogUtil.d(addedFragment + " may be hidden");
            }
            View fragmentView = addedFragment.getView();
            if (fragmentView == null) {
                LogUtil.w("fragmentView = " + null);
                continue;
            }
            ViewGroup viewGroup = (ViewGroup) fragmentView.getParent();
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
     * @param fragmentTag     将显的Fragment的tag
     * @param hideHostIds     将隐的Fragment所在容器Id(可能有多个)
     * @param showHostId      将显的Fragment所在容器Id(仅支持一个)
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, int[] hideHostIds, Integer showHostId, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, null, null, hideHostIds, showHostId, null, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     将显的Fragment的tag
     * @param param1          将显的Fragment的param1
     * @param param2          将显的Fragment的param2
     * @param hideHostIds     将隐的Fragment所在容器Id(可能有多个)
     * @param showHostId      将显的Fragment所在容器Id(仅支持一个)
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, String param1, String param2, int[] hideHostIds, Integer showHostId, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, param1, param2, hideHostIds, showHostId, null, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     将显的Fragment的tag
     * @param hideHostIds     将隐的Fragment所在容器Id(可能有多个)
     * @param showHostId      将显的Fragment所在容器Id(仅支持一个)
     * @param animations      Fragment hide and show 动画{@link FragmentTransaction#setCustomAnimations}
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, int[] hideHostIds, Integer showHostId, int[] animations, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, null, null, hideHostIds, showHostId, animations, animations, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     将显的Fragment的tag
     * @param param1          将显的Fragment的param1
     * @param param2          将显的Fragment的param2
     * @param hideHostIds     将隐的Fragment所在容器Id(可能有多个)
     * @param showHostId      将显的Fragment所在容器Id(仅支持一个)
     * @param animations      Fragment hide and show 动画{@link FragmentTransaction#setCustomAnimations}
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, String param1, String param2, int[] hideHostIds, Integer showHostId, int[] animations, FragmentManager fragmentManager) {
        switchFragment(fragmentTag, param1, param2, hideHostIds, showHostId, animations, animations, fragmentManager);
    }

    /**
     * 切换Fragment
     *
     * @param fragmentTag     将显的Fragment的tag
     * @param param1          将显的Fragment的param1
     * @param param2          将显的Fragment的param2
     * @param hideHostIds     将隐的Fragment所在容器Id(可能有多个)
     * @param showHostId      将显的Fragment所在容器Id(仅支持一个)
     * @param hideAnimations  Fragment hide 动画{@link FragmentTransaction#setCustomAnimations}
     * @param showAnimations  Fragment show 动画{@link FragmentTransaction#setCustomAnimations}
     * @param fragmentManager FragmentManager的实例
     */
    public static void switchFragment(String fragmentTag, String param1, String param2, int[] hideHostIds, Integer showHostId, int[] hideAnimations, int[] showAnimations, FragmentManager fragmentManager) {
        Fragment fragment = getFragment(fragmentTag, param1, param2, fragmentManager);
        LogUtil.d("fragment = " + fragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        setFragmentAnimations(hideAnimations, fragmentTransaction);
        hideFragmentForParent(fragment, fragmentManager.getFragments(), IntStream.of(hideHostIds).boxed().collect(Collectors.toList()), fragmentTransaction);
        if (fragment != null) {
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
