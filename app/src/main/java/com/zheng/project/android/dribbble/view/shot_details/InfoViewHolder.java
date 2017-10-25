package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class InfoViewHolder extends BaseViewHolder{
    @BindView(R.id.shot_title)
    TextView title;
    @BindView(R.id.shot_description) TextView description;
    @BindView(R.id.shot_author_picture)
    ImageView authorPicture;
    @BindView(R.id.shot_author_name) TextView authorName;

    public InfoViewHolder(View itemView) {
        super(itemView);
    }
}
