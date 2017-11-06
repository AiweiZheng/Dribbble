package com.zheng.project.android.dribbble.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.zheng.project.android.dribbble.R;
import java.util.List;

public abstract class InfiniteAdapter<T> extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private boolean showLoading = false;

    private List<T> data;

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
        if (getDisplayWithAnimation()) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_bottom_to_top);
            holder.itemView.startAnimation(animation);
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
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder)
    {
        if (holder instanceof  BaseViewHolder) {
            ((BaseViewHolder)holder).clearAnimation();
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
        notifyItemRangeInserted(0, newData.size());
    }

    public void setData(@NonNull List<T> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void removeData(T item) {
        notifyItemRemoved(data.indexOf(item));
        data.remove(item);
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

    protected boolean getDisplayWithAnimation() {
        return true;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
