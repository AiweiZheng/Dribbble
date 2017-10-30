package com.zheng.project.android.dribbble.view.shot_list;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class ShotViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_clickable_cover) public View cover;
    @BindView(R.id.shot_image) public SimpleDraweeView shotImage;
    @BindView(R.id.gif_text) public TextView gifText;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_view_count) public TextView viewCount;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}
