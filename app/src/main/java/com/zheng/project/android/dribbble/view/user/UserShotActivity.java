package com.zheng.project.android.dribbble.view.user;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.User;
import com.zheng.project.android.dribbble.utils.ModelUtils;
import com.zheng.project.android.dribbble.view.base.SingleFragmentActivity;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

public class UserShotActivity extends SingleFragmentActivity {

    @Override
    public String getActivityTitle() {
        return getUser().name;
    }

    @Override
    protected String getSubtitle() {
        return getString(R.string.shots) + " (" + getUser().shots_count + ")";
    }

    @NonNull
    @Override
    protected Fragment newFragment() {
        Fragment fragment = ShotListFragment.newUserShotListInstance(getUser());
        return fragment;
    }

    private User getUser() {
        return ModelUtils.toObject(getIntent().getStringExtra(UserInfoFragment.KEY_USER),
                new TypeToken<User>(){});
    }
}
