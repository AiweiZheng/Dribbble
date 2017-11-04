package com.zheng.project.android.dribbble.view.user;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.SingleFragmentActivity;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

public class UserShotActivity extends SingleFragmentActivity {
    @Override
    public String getActivityTitle() {
        return getIntent().getStringExtra(UserInfoFragment.KEY_AUTHOR_NAME);
    }

    @Override
    protected String getSubtitle() {
        return getString(R.string.shots) + " (" +
                getIntent().getIntExtra(UserInfoFragment.KEY_AUTHOR_SHOT_COUNT, 0) + ")";
    }

    @NonNull
    @Override
    protected Fragment newFragment() {
        Fragment fragment = ShotListFragment.newUserShotListInstance(
                getIntent().getStringExtra(UserInfoFragment.KEY_USER));
        return fragment;
    }
}
