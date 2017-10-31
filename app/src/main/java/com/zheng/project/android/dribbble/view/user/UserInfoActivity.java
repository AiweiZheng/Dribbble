package com.zheng.project.android.dribbble.view.user;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zheng.project.android.dribbble.view.base.SingleFragmentActivity;

public class UserInfoActivity extends SingleFragmentActivity{
    @NonNull
    @Override
    protected Fragment newFragment() {
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}
