package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class ActionsViewHolder extends BaseViewHolder{
    @BindView(R.id.shot_like_count) TextView likeCount;
    @BindView(R.id.shot_view_count) TextView viewCount;
    @BindView(R.id.shot_bucket_count) TextView bucketCount;
    @BindView(R.id.shot_action_like) ImageButton likeButton;
    @BindView(R.id.shot_action_bucket) ImageButton bucketButton;
    @BindView(R.id.shot_action_share) TextView shareButton;

    public ActionsViewHolder(View itemView) {
        super(itemView);
    }
}
