package com.zheng.project.android.dribbble.view.comment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Comment;
import com.zheng.project.android.dribbble.utils.DateUtils;
import com.zheng.project.android.dribbble.utils.HtmlUtils;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;

import java.util.List;

public class CommentListAdapter extends InfiniteAdapter<Comment>{

    public CommentListAdapter(@NonNull Context context, @NonNull List<Comment> data) {
        super(context, data);
    }

    @Override
    protected BaseViewHolder onCreateView(@NonNull ViewGroup parent) {
          View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false);

          return new CommentViewHolder(view);
    }

    @Override
    protected void onBindView(@NonNull BaseViewHolder vh, int position) {
        Comment comment = getData().get(position);
        CommentViewHolder holder = (CommentViewHolder) vh;

        holder.authorPicture.setImageURI(Uri.parse(comment.user.avatar_url));
        holder.authorName.setText(comment.user.name);
        holder.createdAt.setText(DateUtils.timeAgo(comment.created_at));
        HtmlUtils.setHtmlText(holder.commentBody, comment.body,true);
    }
}
