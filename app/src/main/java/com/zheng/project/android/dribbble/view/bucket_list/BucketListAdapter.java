package com.zheng.project.android.dribbble.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Bucket;

import java.text.MessageFormat;
import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter{

    private List<Bucket> buckets;

    public BucketListAdapter(List<Bucket> buckets) {
        this.buckets = buckets;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bucket, parent, false);

        BucketViewHolder bucketViewHolder = new BucketViewHolder(view);
        return bucketViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bucket bucket = buckets.get(position);

        String bucketShotCountString = MessageFormat.format(
                holder.itemView.getContext().getResources().getString(R.string.shot_count),
                bucket.shots_count);

        BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
        bucketViewHolder.bucketName.setText(bucket.name);
        bucketViewHolder.bucketShotCount.setText(bucketShotCountString);
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }
}
