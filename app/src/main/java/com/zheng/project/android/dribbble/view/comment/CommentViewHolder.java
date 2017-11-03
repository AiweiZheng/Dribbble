package com.zheng.project.android.dribbble.view.comment;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class CommentViewHolder extends BaseViewHolder {
    @BindView(R.id.comment_author_name) TextView authorName;
    @BindView(R.id.comment_author_picture) SimpleDraweeView authorPicture;
    @BindView(R.id.comment_created_at) TextView createdAt;
    @BindView(R.id.comment_text) TextView commentBody;

    public CommentViewHolder(View itemView) {
        super(itemView);
    }
}
