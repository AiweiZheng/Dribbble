package com.zheng.project.android.dribbble.view.shot_details;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.models.User;
import com.zheng.project.android.dribbble.utils.AnimatedImageUtils;
import com.zheng.project.android.dribbble.utils.DateUtils;
import com.zheng.project.android.dribbble.utils.HtmlUtils;
import com.zheng.project.android.dribbble.utils.ModelUtils;
import com.zheng.project.android.dribbble.view.base.CustomTransitionListener;
import com.zheng.project.android.dribbble.view.comment.CommentActivity;
import com.zheng.project.android.dribbble.view.user.UserInfoActivity;
import com.zheng.project.android.dribbble.view.user.UserInfoFragment;

import static com.zheng.project.android.dribbble.view.shot_list.ShotListAdapter.TRANSITION_SHARED_ITEM_NAME_SHOT_IMAGE;

public class ShotAdapter extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_ACTIONS = 1;
    private static final int VIEW_TYPE_SHOT_INFO = 2;

    private static final int NUM_OF_ELEMENT = 3;
    public static final String SHARED_ELEMENT_USER_PICTURE = "shared_element_user_picture";

    private ShotFragment shotFragment;
    private Shot shot;

    public ShotAdapter(@NonNull Shot shot, @NonNull ShotFragment shotFragment) {
        this.shot = shot;
        this.shotFragment = shotFragment;
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
                setupShotImage((ImageViewHolder) holder);
                break;

            case VIEW_TYPE_SHOT_ACTIONS:
                setShotActions((ActionsViewHolder) holder);
                break;

            case VIEW_TYPE_SHOT_INFO:
                setShotInfo((InfoViewHolder) holder);
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

    @NonNull
    private Context getContext() {
        return shotFragment.getContext();
    }

    private void setShotInfo(final InfoViewHolder shotDetailViewHolder) {
        setupShotInfoUI(shotDetailViewHolder);
        shotDetailViewHolder.authorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartUserInfoActivity(shotDetailViewHolder.authorPicture);
            }
        });
    }

    private void setShotActions(ActionsViewHolder actionsViewHolder) {
        setupActionsUI(actionsViewHolder);

        actionsViewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCommentActivity();
            }
        });

        actionsViewHolder.bucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotFragment.bucket();
            }
        });

        actionsViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotFragment.share();
            }
        });
    }

    private void startCommentActivity() {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra(ShotFragment.KEY_SHOT_ID, shot.id);
        intent.putExtra(ShotFragment.KEY_SHOT_TITLE, shot.title);
        intent.putExtra(ShotFragment.KEY_SHOT_COMMENTS_COUNT, shot.comments_count);
        getContext().startActivity(intent);
    }

    private void StartUserInfoActivity(View sharedElement) {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra(UserInfoFragment.KEY_USER,
                ModelUtils.toString(shot.user, new TypeToken<User>(){}));
        intent.putExtra(ShotAdapter.SHARED_ELEMENT_USER_PICTURE, ViewCompat.getTransitionName(sharedElement));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                shotFragment.getActivity(),
                sharedElement,
                ViewCompat.getTransitionName(sharedElement));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            shotFragment.getActivity().startActivity(intent, options.toBundle());
        }
        else {
            shotFragment.startActivity(intent);
        }

//        Context context = vh.itemView.getContext();
//        Intent intent = new Intent(context, ShotActivity.class);
//        intent.putExtra(ShotFragment.KEY_SHOT,
//                ModelUtils.toString(shot, new TypeToken<Shot>() {
//                }));
//        intent.putExtra(TRANSITION_SHARED_ITEM_NAME_SHOT_IMAGE, ViewCompat.getTransitionName(shotVh.shotImage));
//        intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                shotListFragment.getActivity(),
//                shotVh.shotImage,
//                ViewCompat.getTransitionName(shotVh.shotImage));
//
//        shotListFragment.startActivity(intent, options.toBundle());
    }

    private void startZoomShotImageActivity() {
        Intent intent = new Intent(getContext(), ZoomActivity.class);
        intent.putExtra(ZoomFragment.KEY_SHOT_IMAGE_URL, shot.getImageUrl());
        getContext().startActivity(intent);
    }

    private void setupActionsUI(ActionsViewHolder actionsViewHolder) {

        actionsViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        actionsViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        actionsViewHolder.commentButton.setText(String.valueOf(shot.comments_count));

        final int[] stateSet = {android.R.attr.state_checked * (shot.bucketed ? 1 : -1)};
        actionsViewHolder.bucketButton.setImageState(stateSet, true);
    }

    private void setupShotInfoUI(InfoViewHolder shotDetailVh) {
        shotDetailVh.authorName.setText(shot.user.name);
        shotDetailVh.createdAt.setText(DateUtils.timeAgo(shot.created_at));
        shotDetailVh.viewCount.setText(String.valueOf(shot.views_count));

        HtmlUtils.setHtmlText(shotDetailVh.description, shot.description, false);
       // shotDetailViewHolder.authorPicture.setImageURI(Uri.parse(shot.user.avatar_url));
        Picasso.with(shotDetailVh.authorPicture.getContext())
                .load(shot.user.avatar_url)
                .noFade()
                .into(shotDetailVh.authorPicture);

        ViewCompat.setTransitionName(shotDetailVh.authorPicture, shot.user.id);
    }

    private void setupShotImageUI(ImageViewHolder imageViewHolder) {
        AnimatedImageUtils.autoPlayAnimations(shot.getImageUrl(), imageViewHolder.simpleDraweeView);
        //final int[] stateSet = {android.R.attr.state_checked * (shot.liked ? 1 : -1)};

        final int[] stateSet = {android.R.attr.state_checked * (shot.liked ? 1 : -1)};
        imageViewHolder.fab.setImageState(stateSet, true);
    }

    public void setupShotImage(final ImageViewHolder imageViewHolder) {

        setupShotImageUI(imageViewHolder);

        imageViewHolder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startZoomShotImageActivity();
            }
        });

        imageViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shotFragment.like(shot.id, !shot.liked);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = shotFragment.getArguments().getString(TRANSITION_SHARED_ITEM_NAME_SHOT_IMAGE);
            imageViewHolder.simpleDraweeView.setTransitionName(imageTransitionName);

            shotFragment.getActivity().getWindow().getEnterTransition().addListener(new CustomTransitionListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    shotFragment.getActivity().getWindow().getEnterTransition().removeListener(this);
                    imageViewHolder.fab.animate().scaleX(1).scaleY(1);
                }
                @Override
                public void onTransitionStart(Transition transition) {
                    imageViewHolder.fab.setScaleX(0);
                    imageViewHolder.fab.setScaleY(0);
                }
            });
        }
    }
}
