package com.zheng.project.android.dribbble.view;

import com.zheng.project.android.dribbble.base.DataListFragment;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

public class DataListFragmentFactory {

    public static int SHOT_LIST_FRAGMENT = 0;
    public static int BUCKET_LIST_FRAGMENT = 1;

    public static DataListFragment getFragment(int type) {
        if (type == SHOT_LIST_FRAGMENT) {
            return ShotListFragment.newInstance();
        }
//        if (type == BUCKET_LIST_FRAGMENT) {
//            return BucketListFragment.newInstance();
//        }
        return null;
    }
}
