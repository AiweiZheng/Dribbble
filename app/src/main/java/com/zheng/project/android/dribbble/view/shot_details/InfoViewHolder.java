package com.zheng.project.android.dribbble.view.shot_details;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.TranslateDraweeView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoViewHolder extends BaseViewHolder{
    @BindView(R.id.shot_created_at) TextView createdAt;
    @BindView(R.id.shot_view_count) TextView viewCount;
    @BindView(R.id.shot_description) TextView description;
    @BindView(R.id.shot_author_picture) CircleImageView authorPicture;
    @BindView(R.id.shot_author_name) TextView authorName;

    public InfoViewHolder(View itemView) {
        super(itemView);
    }
}
