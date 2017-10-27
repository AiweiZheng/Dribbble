package com.zheng.project.android.dribbble.view;

import com.zheng.project.android.dribbble.view.base.InfiniteFragment;
import com.zheng.project.android.dribbble.view.bucket_list.BucketListFragment;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

public class DataListFragmentFactory {

    public static int SHOT_LIST_FRAGMENT = 0;
    public static int BUCKET_LIST_FRAGMENT = 1;

    public static InfiniteFragment getFragment(int type) {
        if (type == SHOT_LIST_FRAGMENT) {
            return ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR);
        }
        if (type == BUCKET_LIST_FRAGMENT) {
            return BucketListFragment.newInstance(null, false, null);
        }
        return null;
    }
}
