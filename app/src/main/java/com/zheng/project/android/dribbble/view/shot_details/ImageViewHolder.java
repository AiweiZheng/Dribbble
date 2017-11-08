package com.zheng.project.android.dribbble.view.shot_details;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.TranslateDraweeView;

import butterknife.BindView;

public class ImageViewHolder extends BaseViewHolder{
    @BindView(R.id.shot_image) public TranslateDraweeView simpleDraweeView;
    @BindView(R.id.like_fab) public FloatingActionButton fab;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }
}
