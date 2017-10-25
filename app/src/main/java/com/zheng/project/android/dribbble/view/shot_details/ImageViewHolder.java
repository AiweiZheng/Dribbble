package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageView;

import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

public class ImageViewHolder extends BaseViewHolder{
    public ImageView imageview;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageview = (ImageView) itemView;
    }
}
