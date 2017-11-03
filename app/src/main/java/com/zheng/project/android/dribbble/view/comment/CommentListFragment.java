package com.zheng.project.android.dribbble.view.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.models.Comment;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.view.base.InfiniteFragment;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;

import java.util.ArrayList;
import java.util.List;

public class CommentListFragment extends InfiniteFragment<Comment> {

    public static Fragment newInstance(Bundle args) {
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected List<Comment> refreshData() throws DribbbleException {
        return Dribbble.getComments(getArguments().getString(ShotFragment.KEY_SHOT_ID), 0);
    }

//
//    @Override
//    protected int getItemSpace() {
//        return 0;
//    }

    @Override
    protected List<Comment> loadMoreData(int dataSize) throws DribbbleException {
        return Dribbble.getComments(getArguments()
                .getString(ShotFragment.KEY_SHOT_ID), dataSize / Dribbble.COUNT_PER_PAGE + 1);
    }

    @NonNull
    @Override
    protected InfiniteAdapter createAdapter() {
        return new CommentListAdapter(getContext(), new ArrayList<Comment>());
    }

    @NonNull
    @Override
    protected View createView(@Nullable ViewGroup container) {
        return getLayoutInflater().inflate(R.layout.fragment_recycler_view, container, false);
    }
}
