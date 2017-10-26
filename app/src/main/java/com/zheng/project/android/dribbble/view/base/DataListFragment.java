package com.zheng.project.android.dribbble.view.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class DataListFragment<T> extends Fragment {

    public static final int REQ_CODE_NEW_BUCKET = 100;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    private DataListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = createView(container);
        ButterKnife.bind(this, view);
      //  swipeRefreshLayout.setEnabled(false); //not allow to refresh before first shot load finished.
        viewCreated();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                new LoadDataTask(DataListFragment.this, true).execute();
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources()
                                       .getDimensionPixelOffset(R.dimen.spacing_medium)));

        adapter = createAdapter();

        adapter.loadMoreListener = new DataListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadDataTask(DataListFragment.this, false).execute();
            }
        };

        recyclerView.setAdapter(adapter);
    }

    protected abstract List<T> refreshData() throws DribbbleException;

    protected abstract List<T> loadMoreData(int dataSize) throws DribbbleException;

    @NonNull
    protected abstract DataListAdapter createAdapter();

    @NonNull
    protected abstract View createView(@Nullable ViewGroup container);

    @NonNull
    protected void onDataFetched(List<T> newData) {}

    protected void viewCreated(){}

    //////////////////////////Load data Async task//////////////////////////////////////
    private class LoadDataTask extends BackgroundTask<Void, Void, List<T>> {

        private DataListFragment dataListFragment;
        private boolean refresh = false;

        public LoadDataTask(DataListFragment dataListFragment, boolean refresh) {
            this.dataListFragment = dataListFragment;
            this.refresh = refresh;
        }


        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        @Override
        protected List<T> doJob() throws DribbbleException {
            List<T> moreData;
            if (refresh) {
                moreData = dataListFragment.refreshData();
            } else {
                moreData = dataListFragment.loadMoreData(dataListFragment.adapter.getDataCount());
            }
            return moreData;
        }

        @Override
        protected void onSuccess(List<T> data) {
            if (data == null) {
                Log.error(dataListFragment.getView(), "Error when load data").show();
                return;
            }

            dataListFragment.adapter.setShowLoading(data.size() >= Dribbble.COUNT_PER_PAGE);
            onDataFetched(data);
            if (refresh) { //refresh
                dataListFragment.adapter.setData(data);
                dataListFragment.swipeRefreshLayout.setRefreshing(false);// stop showing the refreshing symbol.
            } else { //load more data
                dataListFragment.adapter.append(data);
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Log.error(getView(), e.getMessage());
        }
    }
}
