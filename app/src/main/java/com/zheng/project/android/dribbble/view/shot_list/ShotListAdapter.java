package com.zheng.project.android.dribbble.view.shot_list;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.view.shot_details.ShotActivity;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_SHOT = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Shot> shots;
    private LoadMoreListener loadMoreListener;
    private boolean showLoading = false;

    public ShotListAdapter(List<Shot> shots, @NonNull LoadMoreListener loadMoreListener) {
        this.shots = shots;
        this.showLoading = true;
        this.loadMoreListener = loadMoreListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_SHOT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_shot, parent, false);

                ShotViewHolder shotViewHolder = new ShotViewHolder(view);
                return shotViewHolder;
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
        if (position >= shots.size()) {
            loadMoreListener.onLoadMore();
        }

        if (holder instanceof ShotViewHolder) {

            ShotViewHolder shotVh = (ShotViewHolder) holder;
            final Shot shot = shots.get(position);

            shotVh.shotImage.setImageURI(Uri.parse(shot.getImageUrl()));
            shotVh.bucketCount.setText(String.valueOf((shot.bucketsCount)));
            shotVh.viewCount.setText(String.valueOf((shot.viewsCount)));
            shotVh.likeCount.setText(String.valueOf((shot.likesCount)));

            shotVh.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = holder.itemView.getContext();
                    Intent intent = new Intent(context, ShotActivity.class);
                    intent.putExtra(ShotFragment.KEY_SHOT,
                            ModelUtils.toString(shot, new TypeToken<Shot>() {
                            }));
                    intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);

                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < shots.size()) {
            return VIEW_TYPE_SHOT;
        }
        else {
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? shots.size() + 1 : shots.size();
    }

    public int getDataCount() {
        return shots.size();
    }

    public void append(@NonNull List<Shot> newShots) {
        shots.addAll(newShots);
    }

    public void setData(@NonNull List<Shot> newShots) {
        shots.clear();
        shots.addAll(newShots);
        notifyDataSetChanged();
    }
    public void setShowLoading(boolean showLoading) {
        if (this.showLoading != showLoading) {
            this.showLoading = showLoading;
            notifyDataSetChanged();
        }
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
