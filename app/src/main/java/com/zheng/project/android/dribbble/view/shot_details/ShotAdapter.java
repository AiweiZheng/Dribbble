package com.zheng.project.android.dribbble.view.shot_details;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.utils.AnimatedImageUtils;


public class ShotAdapter extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_ACTIONS = 1;
    private static final int VIEW_TYPE_SHOT_INFO = 2;

    private static final int NUM_OF_ELEMENT = 3;
    private Shot shot;

    public ShotAdapter(@NonNull Shot shot) {
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_SHOT_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_item_image, parent, false);
                ImageViewHolder imageViewHolder = new ImageViewHolder(view);
                return imageViewHolder;

            case VIEW_TYPE_SHOT_ACTIONS:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_item_actions, parent, false);
                ActionsViewHolder actionsViewHolder = new ActionsViewHolder(view);
                return actionsViewHolder;

            case VIEW_TYPE_SHOT_INFO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_item_info, parent, false);
                InfoViewHolder infoViewHolder = new InfoViewHolder(view);
                return infoViewHolder;
            default:
                return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SHOT_IMAGE:
                AnimatedImageUtils.autoPlayAnimations(shot.getImageUrl(),
                        ((ImageViewHolder) holder).imageview);
                break;

            case VIEW_TYPE_SHOT_ACTIONS:
                ActionsViewHolder actionsViewHolder = (ActionsViewHolder) holder;
                actionsViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
                actionsViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
                actionsViewHolder.viewCount.setText(String.valueOf(shot.views_count));
                break;

            case VIEW_TYPE_SHOT_INFO:
                InfoViewHolder shotDetailViewHolder = (InfoViewHolder) holder;
                shotDetailViewHolder.title.setText(shot.title);
                shotDetailViewHolder.authorName.setText(shot.user.name);
                shotDetailViewHolder.description.setText(Html.fromHtml(shot.description == null
                                                                       ? "" : shot.description));
                shotDetailViewHolder.authorPicture.setImageURI(Uri.parse(shot.user.avatar_url));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE_SHOT_IMAGE) {
            return VIEW_TYPE_SHOT_IMAGE;
        }
        else if (position == VIEW_TYPE_SHOT_ACTIONS) {
            return VIEW_TYPE_SHOT_ACTIONS;
        }
        else {
            return VIEW_TYPE_SHOT_INFO;
        }
    }

    @Override
    public int getItemCount() {return NUM_OF_ELEMENT;}
}
