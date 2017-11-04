package com.zheng.project.android.dribbble.view.comment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.SingleFragmentActivity;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;

public class CommentActivity extends SingleFragmentActivity{

    @Override
    public String getActivityTitle() {
        return getIntent().getStringExtra(ShotFragment.KEY_SHOT_TITLE);
    }

    @Override
    protected String getSubtitle() {
        return getString(R.string.comments) + " (" +
                getIntent().getIntExtra(ShotFragment.KEY_SHOT_COMMENTS_COUNT, 0) + ")";
    }

    @NonNull
    @Override
    protected Fragment newFragment() {
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}

