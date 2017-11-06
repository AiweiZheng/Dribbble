package com.zheng.project.android.dribbble.view.bucket_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
    private BucketOptionsMenu.EditBucketListener editBucketListener;
    private BucketOptionsMenu.DeleteBucketListener deleteBucketListener;

    public BucketListAdapter(@NonNull Context context,
                             List<Bucket> buckets,
                             boolean isEditingMode,
                             @NonNull BucketOptionsMenu.EditBucketListener editBucketListener,
                             @NonNull BucketOptionsMenu.DeleteBucketListener deleteBucketListener) {

        super(context, buckets);

        this.editBucketListener = editBucketListener;
        this.deleteBucketListener = deleteBucketListener;
        this.isEditingMode = isEditingMode;
    }

    @Override
    protected boolean getDisplayWithAnimation() {
        return false;
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
        final Bucket bucket = getData().get(position);

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
        final Bucket bucket = getData().get(position);

        bindBasicInfo(bucketViewHolder, bucket);
        bucketViewHolder.bucketChosen.setVisibility(View.GONE);
        bucketViewHolder.bucketOptionsMenu.setVisibility(View.VISIBLE);

        bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBucketShotListActivity(bucket);// display the shots in the bucket;
            }
        });

        final ImageView optionsMenu = bucketViewHolder.bucketOptionsMenu;
        optionsMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BucketOptionsMenu.newInstance(getContext(),
                                              optionsMenu,
                                              bucket,
                                              editBucketListener,
                                              deleteBucketListener).show();//show popup menu
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

    public void removeBucket(@NonNull String bucketId) {
        removeData(findBucket(bucketId));
    }

    public void updateBucket(@NonNull String bucketId, @NonNull String name, @NonNull String description) {
        Bucket bucket = findBucket(bucketId);
        bucket.name = name;
        bucket.description = description;
        notifyItemChanged(getData().indexOf(bucket));
    }

    @NonNull
    private Bucket findBucket(@NonNull String bucketId) {
        for (Bucket bucket : getData()) {
            if (bucket.id.equals(bucketId)) {
                return bucket;
            }
        }
        return null;
    }

    private String formatShotCount(int shotCount) {
        return shotCount <= 1
                ? getContext().getString(R.string.shot_count_single, shotCount)
                : getContext().getString(R.string.shot_count_plural, shotCount);
    }
}
