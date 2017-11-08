package com.zheng.project.android.dribbble.view.user;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.models.User;
import com.zheng.project.android.dribbble.utils.Displayer;
import com.zheng.project.android.dribbble.utils.HtmlUtils;
import com.zheng.project.android.dribbble.utils.ModelUtils;
import com.zheng.project.android.dribbble.view.base.CustomTransitionListener;
import com.zheng.project.android.dribbble.view.base.TranslateDraweeView;
import com.zheng.project.android.dribbble.view.shot_details.ShotAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoFragment extends Fragment{
    @BindView(R.id.user_detail_bio) TextView bio;
    @BindView(R.id.user_detail_bucket_count) TextView bucketCount;
    @BindView(R.id.user_detail_followers_count) TextView followerCount;
    @BindView(R.id.user_detail_following_count) TextView followingCount;
    @BindView(R.id.user_detail_like_count) TextView likeCount;
    @BindView(R.id.user_detail_location) TextView location;
    @BindView(R.id.user_detail_shots_count) TextView shotCount;
    @BindView(R.id.user_detail_name) TextView name;
    @BindView(R.id.user_detail_project_count) TextView projectCount;
    @BindView(R.id.user_detail_user_picture) SimpleDraweeView picture;
    @BindView(R.id.user_detail_layout) LinearLayout detailLayout;

    public static final String KEY_USER = "author";

    private User author;
    private boolean isCheckingFollowing;
    private boolean isFollowing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isCheckingFollowing = true;
        return getLayoutInflater().inflate(R.layout.user_details, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Dribbble.isLoggedIn()) {
            inflater.inflate(R.menu.user_follow_menu, menu);
            new CheckIfFollowingAuthorTask(author.id, menu.findItem(R.id.follow)).execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.follow) {
            if (isCheckingFollowing) {
                Displayer.showOnSnackBar(getView(), getString(R.string.user_detail_checking_following));
            }
            else {
                if (isFollowing) {
                    toggleAnimation(true, item);
                    new UnfollowAuthorTask(author.id, item).execute();
                }
                else {
                    toggleAnimation(true, item);
                    new FollowAuthorTask(author.id, item).execute();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getEnterTransition().addListener(new CustomTransitionListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    getActivity().getWindow().getEnterTransition().removeListener(this);
                    detailLayout.animate().scaleX(1).scaleY(1);
                }
                @Override
                public void onTransitionStart(Transition transition) {
                    detailLayout.animate().scaleX(0).scaleY(0);
                }
            });
        }

        author = ModelUtils.toObject(getArguments().getString(KEY_USER), new TypeToken<User>(){});
        picture.setImageURI(Uri.parse(author.avatar_url));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getArguments().getString(ShotAdapter.SHARED_ELEMENT_USER_PICTURE);
            picture.setTransitionName(imageTransitionName);
        }

        name.setText(author.name);
        location.setText(author.location);
        HtmlUtils.setHtmlText(bio, author.bio, true);

        projectCount.setText(formatProjectsCount(author.projects_count));
        followerCount.setText(formatFollowersCount(author.followers_count));
        followingCount.setText(formatFollowingCount(author.followings_count));
        shotCount.setText(formatShotsCount(author.shots_count));

        shotCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserShotActivity.class);
                intent.putExtra(KEY_USER, ModelUtils.toString(author, new TypeToken<User>(){}));
                startActivity(intent);
            }
        });

        bucketCount.setText(formatBucketsCount(author.buckets_count));
        likeCount.setText(formatLikesCount(author.likes_count));
    }

    private String formatShotsCount(int shotCount) {
        return shotCount <= 1
                ? getContext().getString(R.string.shot_count_single, shotCount)
                : getContext().getString(R.string.shot_count_plural, shotCount);
    }

    private String formatProjectsCount(int projectCount) {
        return projectCount <= 1
                ? getContext().getString(R.string.project_count_single, projectCount)
                : getContext().getString(R.string.project_count_plural, projectCount);
    }

    private String formatFollowersCount(int followerCount) {
        return followerCount <= 1
                ? getContext().getString(R.string.follower_count_single, followerCount)
                : getContext().getString(R.string.follower_count_plural, followerCount);
    }

    private String formatFollowingCount(int followingCount) {
        return followingCount <= 1
                ? getContext().getString(R.string.following_count_single, followingCount)
                : getContext().getString(R.string.following_count_plural, followingCount);
    }

    private String formatBucketsCount(int bucketCount) {
        return bucketCount <= 1
                ? getContext().getString(R.string.bucket_count_single, bucketCount)
                : getContext().getString(R.string.bucket_count_plural, bucketCount);
    }

    private String formatLikesCount(int likeCount) {
        return likeCount <= 1
                ? getContext().getString(R.string.like_count_single, likeCount)
                : getContext().getString(R.string.like_count_plural, likeCount);
    }

    private void updateFollowAuthorItemTitle(boolean following, MenuItem followItem){
        isFollowing = following;
        if (following) {
            followItem.setTitle(getString(R.string.following));
        }
        else {
            followItem.setTitle(getString(R.string.follow));
        }
    }

    private void toggleAnimation(boolean isStart, MenuItem item) {
//        if (isStart) {
//            Drawable drawable  = getResources().getDrawable(R.drawable.ic_download_animation);
//            item.setIcon(drawable);
//            ((Animatable) drawable).start();
//        }
//        else {
//            Drawable drawable = item.getIcon();
//            if (drawable instanceof Animatable) {
//                ((Animatable) drawable).stop();
//                item.setIcon(null);
//            }
//        }
    }

    /**************************************background task******************************/
    private class CheckIfFollowingAuthorTask extends BackgroundTask<Void, Void, Boolean> {

        private String authorId;
        private MenuItem followItem;

        public CheckIfFollowingAuthorTask(String authorId, MenuItem followItem) {
            this.authorId = authorId;
            this.followItem = followItem;
        }

        @Override
        protected void onSuccess(Boolean result) {
            updateFollowAuthorItemTitle(result, followItem);
            isCheckingFollowing = false;
        }

        @Override
        protected void onFailed(DribbbleException e) {
            isCheckingFollowing = false;
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Boolean doJob(Void... voids) throws DribbbleException {
            isCheckingFollowing = true;
            return Dribbble.isFollowing(authorId);
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class FollowAuthorTask extends BackgroundTask<Void, Void, Void> {
        private String authorId;
        private MenuItem followItem;

        public FollowAuthorTask(String authorId, MenuItem followItem) {
            this.authorId = authorId;
            this.followItem = followItem;
        }
        @Override
        protected void onSuccess(Void aVoid) {
            isCheckingFollowing = false;
            toggleAnimation(false, followItem);
            updateFollowAuthorItemTitle(true, followItem);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            isCheckingFollowing = false;
            toggleAnimation(false, followItem);
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Void doJob(Void... voids) throws DribbbleException {
            isCheckingFollowing = true;
            Dribbble.follow(authorId);
            return null;
        }
        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class UnfollowAuthorTask extends BackgroundTask<Void, Void, Void> {

        private String authorId;
        private MenuItem followItem;

        public UnfollowAuthorTask(String authorId, MenuItem followItem) {
            this.authorId = authorId;
            this.followItem = followItem;
        }

        @Override
        protected void onSuccess(Void aVoid) {
            isCheckingFollowing = false;
            toggleAnimation(false, followItem);
            updateFollowAuthorItemTitle(false, followItem);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            isCheckingFollowing = false;
            toggleAnimation(false, followItem);
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Void doJob(Void... voids) throws DribbbleException {
            isCheckingFollowing = true;
            Dribbble.unfollow(authorId);
            return null;
        }
        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
