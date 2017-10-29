package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class ImageViewHolder extends BaseViewHolder{
    @BindView(R.id.shot_image) public SimpleDraweeView simpleDraweeView;
    @BindView(R.id.progress_bar) public ProgressBar progressBar;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }
}
