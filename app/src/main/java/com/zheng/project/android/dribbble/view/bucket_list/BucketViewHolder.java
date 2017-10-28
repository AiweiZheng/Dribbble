package com.zheng.project.android.dribbble.view.bucket_list;

import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class BucketViewHolder extends BaseViewHolder{

    @BindView(R.id.bucket_layout) RelativeLayout bucketLayout;
    @BindView(R.id.bucket_name) TextView bucketName;
    @BindView(R.id.bucket_shot_count) TextView bucketShotCount;
    @BindView(R.id.bucket_shot_chosen) ImageView bucketChosen;
    @BindView(R.id.bucket_options_menu) ImageView bucketOptionsMenu;

    public BucketViewHolder(View itemView) {
        super(itemView);
    }
}
