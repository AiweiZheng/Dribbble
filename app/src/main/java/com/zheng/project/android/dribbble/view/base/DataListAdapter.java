package com.zheng.project.android.dribbble.view.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;

import java.util.List;

public abstract class DataListAdapter<T> extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<T> data;
    private boolean showLoading = false;

    public LoadMoreListener loadMoreListener;

    public DataListAdapter(@NonNull List<T> data) {
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
            onBindView((BaseViewHolder) holder, data.get(position));
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

    protected abstract BaseViewHolder onCreateView(@NonNull ViewGroup parent);
    protected abstract void onBindView(@NonNull BaseViewHolder vh, @NonNull T data);

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
