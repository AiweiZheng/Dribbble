package com.zheng.project.android.dribbble.view.bucket_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Bucket;

class BucketOptionsMenu extends  PopupMenu{

    public static BucketOptionsMenu newInstance(@NonNull Context context,
                                                @NonNull View anchor,
                                                @NonNull Bucket bucket,
                                                @NonNull DeleteBucketListener deleteBucketListener) {

        return new BucketOptionsMenu(context, anchor, bucket, deleteBucketListener);
    }

    private BucketOptionsMenu(@NonNull Context context,
                              @NonNull View anchor,
                              @NonNull final Bucket bucket,
                              @NonNull final DeleteBucketListener deleteBucketListener) {
        super(context, anchor);

        getMenuInflater().inflate(R.menu.bucket_options_pop_menu, getMenu());

        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.bucket_edit:
                        handleEditBucketRequest(bucket);
                        break;
                    case R.id.bucket_delete:
                        deleteBucketListener.onDeleteBucket(bucket);
                        break;
                }
                return true;
            }
        });
    }


    private void handleEditBucketRequest(Bucket bucket) {

    }

    public interface DeleteBucketListener {
        void onDeleteBucket(Bucket bucket);
    }
}
