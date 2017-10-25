package com.zheng.project.android.dribbble.view.shot_list;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;
import com.zheng.project.android.dribbble.DribbboApplication;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
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

    @NonNull
    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }

    @NonNull
    @Override
    protected List<Shot> onRefresh() {
        try {
            return Dribbble.getShots(0);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    protected List<Shot> onLoadMore(int dataSize) {
        try {
            return Dribbble.getShots(dataSize / Dribbble.COUNT_PER_PAGE + 1);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    protected DataListAdapter onCreateAdapter() {
        return new ShotListAdapter(new ArrayList<Shot>());
    }
}
