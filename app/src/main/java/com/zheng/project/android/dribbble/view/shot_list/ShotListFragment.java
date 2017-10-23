package com.zheng.project.android.dribbble.view.shot_list;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.zheng.project.android.dribbble.DribbboApplication;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.base.DataListAdapter;
import com.zheng.project.android.dribbble.base.DataListFragment;
import com.zheng.project.android.dribbble.models.Shot;

import java.util.List;

import butterknife.BindView;

public class ShotListFragment extends DataListFragment<Shot> {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @Override
    protected List<Shot> onRefresh() {
        return DribbboApplication.fakeShotData(0);
    }

    @NonNull
    @Override
    protected List<Shot> onLoadMore(int dataSize) {
        return DribbboApplication.fakeShotData(dataSize / DribbboApplication.COUNT_PER_PAGE);
    }

    @NonNull
    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }

    @NonNull
    @Override
    protected DataListAdapter onCreateAdapter() {
        return new ShotListAdapter(DribbboApplication.fakeShotData(0));
    }
}
