package com.zheng.project.android.dribbble.view.bucket_list;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Bucket;

class BucketOptionsMenu {

    public static PopupMenu getMenu(Context context, View view, final Bucket bucket) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(context, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.bucket_options_pop_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.bucket_edit:
                        handleEditBucketRequest(bucket);
                        break;
                    case R.id.bucket_delete:
                        handleDeleteBucketRequest(bucket);
                        break;
                }
                return true;
            }
        });

        return popup;
    }

    private static void handleEditBucketRequest(Bucket bucket) {

    }

    private static void handleDeleteBucketRequest(Bucket bucket) {

    }
}
