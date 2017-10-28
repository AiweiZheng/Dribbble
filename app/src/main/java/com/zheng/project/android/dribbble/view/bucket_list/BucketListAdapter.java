package com.zheng.project.android.dribbble.view.bucket_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Bucket;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

import java.util.List;


public class BucketListAdapter extends InfiniteAdapter<Bucket> {

    private boolean isEditingMode;

    public BucketListAdapter(@NonNull Context context, List<Bucket> buckets, boolean isEditingMode) {
        super(context, buckets);
        this.isEditingMode = isEditingMode;
    }

    @Override
    protected BaseViewHolder onCreateView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bucket, parent, false);

        BucketViewHolder bucketViewHolder = new BucketViewHolder(view);
        return bucketViewHolder;
    }

    @Override
    protected void onBindView(@NonNull BaseViewHolder holder, final int position) {

        BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;

        if (isEditingMode) {
            bindViewOnEditMode(bucketViewHolder, position);
        } else {
            bindViewOnViewMode(bucketViewHolder, position);
        }
    }

    private void bindViewOnEditMode(BucketViewHolder bucketViewHolder, final int position) {
        final Bucket bucket = data.get(position);

        bindBasicInfo(bucketViewHolder, bucket);
        bucketViewHolder.bucketChosen.setVisibility(View.VISIBLE);
        bucketViewHolder.bucketOptionsMenu.setVisibility(View.GONE);
        bucketViewHolder.bucketChosen.setImageDrawable(getBucketChosenImage(bucket.isChosen));

        bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bucket.isChosen = !bucket.isChosen;
                notifyItemChanged(position);
            }
        });
    }

    private void bindViewOnViewMode(BucketViewHolder bucketViewHolder, final int position) {
        final Bucket bucket = data.get(position);

        bindBasicInfo(bucketViewHolder, bucket);
        bucketViewHolder.bucketChosen.setVisibility(View.GONE);
        bucketViewHolder.bucketOptionsMenu.setVisibility(View.VISIBLE);

        final ImageView optionsMenu = bucketViewHolder.bucketOptionsMenu;
        optionsMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BucketOptionsMenu.getMenu(getContext(), optionsMenu, bucket).show();//showing popup menu
            }
        });

        bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBucketShotListActivity(bucket);// display the shots in the bucket;
            }
        });
    }

    private void startBucketShotListActivity(Bucket bucket) {
        Intent intent = new Intent(getContext(), BucketShotListActivity.class);
        intent.putExtra(ShotListFragment.KEY_BUCKET_ID, bucket.id);
        intent.putExtra(BucketShotListActivity.KEY_BUCKET_NAME, bucket.name);
        getContext().startActivity(intent);
    }

    private void bindBasicInfo(BucketViewHolder bucketViewHolder, Bucket bucket) {
        bucketViewHolder.bucketName.setText(bucket.name);
        bucketViewHolder.bucketShotCount.setText(formatShotCount(bucket.shots_count));
    }

    private Drawable getBucketChosenImage(boolean isChosen) {
        if (isChosen) {
            return ContextCompat.getDrawable(getContext(), R.drawable.ic_check_box_black_24dp);
        }

        return ContextCompat.getDrawable(getContext(), R.drawable.ic_check_box_outline_blank_black_24dp);
    }

    private String formatShotCount(int shotCount) {
        return shotCount == 0
                ? getContext().getString(R.string.shot_count_single, shotCount)
                : getContext().getString(R.string.shot_count_plural, shotCount);
    }
}
