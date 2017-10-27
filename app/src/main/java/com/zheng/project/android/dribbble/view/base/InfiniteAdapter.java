package com.zheng.project.android.dribbble.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Bucket;

import java.util.List;

public abstract class InfiniteAdapter<T> extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private boolean showLoading = false;

    protected List<T> data;

    public LoadMoreListener loadMoreListener;

    public InfiniteAdapter(@NonNull Context context, @NonNull List<T> data) {
        this.context = context;
        this.data = data;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_DATA:
                return onCreateView(parent);

            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_loading, parent, false);
                return new RecyclerView.ViewHolder(view){};
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_LOADING && loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }

        if (viewType == VIEW_TYPE_DATA) {
            onBindView((BaseViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return VIEW_TYPE_DATA;
        }
        else {
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    public int getDataCount() {
        return data.size();
    }

    public void append(@NonNull List<T> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void prepend(@NonNull List<T> newData) {
        data.addAll(0, newData);
        notifyDataSetChanged();
    }
    public void setData(@NonNull List<T> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public void setShowLoading(boolean showLoading) {
        if (this.showLoading != showLoading) {
            this.showLoading = showLoading;
            notifyDataSetChanged();
        }
    }

    protected Context getContext() {
        return context;
    }

    public List<T> getData() {
        return data;
    }
    protected abstract BaseViewHolder onCreateView(@NonNull ViewGroup parent);
    protected abstract void onBindView(@NonNull BaseViewHolder vh, int position);
    public interface LoadMoreListener {
        void onLoadMore();
    }
}
