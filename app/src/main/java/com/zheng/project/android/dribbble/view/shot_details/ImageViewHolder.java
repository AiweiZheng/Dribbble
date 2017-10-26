package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

public class ImageViewHolder extends BaseViewHolder{
    public SimpleDraweeView imageview;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageview = (SimpleDraweeView) itemView;
    }
}
