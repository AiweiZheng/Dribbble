package com.zheng.project.android.dribbble.shot_list;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Shot;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter{

    private List<Shot> shots;

    public ShotListAdapter(List<Shot> shots) {
        this.shots = shots;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shot, parent, false);

        ShotViewHolder shotViewHolder = new ShotViewHolder(view);
        return shotViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShotViewHolder shotVh = (ShotViewHolder) holder;
        Shot shot = shots.get(position);

        shotVh.bucketCount.setText(String.valueOf((shot.bucketCount)));
        shotVh.viewCount.setText(String.valueOf((shot.viewCount)));
        shotVh.likeCount.setText(String.valueOf((shot.likeCount)));
    }

    @Override
    public int getItemCount() {
        return shots.size();
    }
}
