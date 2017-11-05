package com.zheng.project.android.dribbble.view.shot_details;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.SingleFragmentActivity;

public class ZoomActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        return ZoomFragment.newInstance(getIntent().getExtras());
    }

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getString(R.string.image_details);
    }
}
