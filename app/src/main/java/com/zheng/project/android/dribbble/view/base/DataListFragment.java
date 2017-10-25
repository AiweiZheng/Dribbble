package com.zheng.project.android.dribbble.view.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.zheng.project.android.dribbble.utils.BackgroundTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class DataListFragment<T> extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    private DataListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
     //   swipeRefreshLayout.setEnabled(false); //not allow to refresh before first shot load finished.

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                new LoadDataTask(true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        adapter = onCreateAdapter();

        adapter.loadMoreListener = new DataListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @NonNull
    protected abstract List<T> onRefresh();

    @NonNull
    protected abstract List<T> onLoadMore(int dataSize);

    @NonNull
    protected abstract DataListAdapter onCreateAdapter();

    private class LoadDataTask extends BackgroundTask<List<T>> {

        private boolean refresh = false;

        public LoadDataTask(boolean refresh) {
            this.refresh = refresh;
        }
        public LoadDataTask(){ this.refresh = false;}

        @Override
        protected List<T> executeInBackGround() {
            List<T> moreData;
            if (refresh) {
                moreData = onRefresh();
            }
            else {
                moreData = onLoadMore(adapter.getDataCount());
            }
            return moreData;
        }

        @Override
        protected void onPost(List<T> data) {
            if (data == null) {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (refresh) { //refresh
                adapter.setData(data);
                swipeRefreshLayout.setRefreshing(false);// stop showing the refreshing symbol.
            }
            else { //load more data
                adapter.append(data);
                adapter.setShowLoading(data.size() >= Dribbble.COUNT_PER_PAGE);
            }
        }
    }
}
