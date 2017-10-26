package com.zheng.project.android.dribbble.view.shot_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonSyntaxException;
import com.zheng.project.android.dribbble.DribbboApplication;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.view.base.DataListAdapter;
import com.zheng.project.android.dribbble.view.base.DataListFragment;
import com.zheng.project.android.dribbble.models.Shot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShotListFragment extends DataListFragment<Shot> {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    public static final String KEY_LIST_TYPE = "list_type";
    public static final String KEY_BUCKET_ID = "bucket_id";

    public static final int LIST_TYPE_POPULAR = 1;
    public static final int LIST_TYPE_LIKED = 2;
    public static final int LIST_TYPE_BUCKET = 3;

    @NonNull
    public static ShotListFragment newInstance(int listType) {
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, listType);

        ShotListFragment fragment = new ShotListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newBucketListInstance(String bucketId) {
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, LIST_TYPE_BUCKET);
        args.putString(KEY_BUCKET_ID, bucketId);

        ShotListFragment fragment = new ShotListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Shot> fetchData(int listStyle, int page) throws DribbbleException {
        switch (listStyle) {
            case LIST_TYPE_POPULAR:
                return Dribbble.getShots(page);
            case LIST_TYPE_LIKED:
                return Dribbble.getLikedShots(page);
            case LIST_TYPE_BUCKET:
                String bucketId = getArguments().getString(KEY_BUCKET_ID);
                return Dribbble.getBucketShots(bucketId, page);
        }
        return null;
    }

    @Override
    protected List<Shot> refreshData() throws DribbbleException {
        return fetchData(getArguments().getInt(KEY_LIST_TYPE), 1);
    }

    @Override
    protected List<Shot> loadMoreData(int dataSize) throws DribbbleException {
        return fetchData(getArguments().getInt(KEY_LIST_TYPE), dataSize / Dribbble.COUNT_PER_PAGE + 1);
    }

    @NonNull
    @Override
    protected DataListAdapter createAdapter() {
        return new ShotListAdapter(getContext(), new ArrayList<Shot>());
    }

    @NonNull
    @Override
    protected View createView(@Nullable ViewGroup container) {
        return getLayoutInflater().inflate(R.layout.fragment_recycler_view, container, false);
    }


}
