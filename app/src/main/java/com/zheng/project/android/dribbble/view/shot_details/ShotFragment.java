package com.zheng.project.android.dribbble.view.shot_details;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.models.Bucket;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.utils.Displayer;
import com.zheng.project.android.dribbble.utils.ModelUtils;
import com.zheng.project.android.dribbble.view.bucket_list.BucketListActivity;
import com.zheng.project.android.dribbble.view.bucket_list.BucketListFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotFragment extends Fragment{
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    public static final String KEY_SHOT = "shot";
    private static final int REQ_CODE_BUCKET = 100;

    private Shot shot;
    private boolean isLiking;
    private ArrayList<String> collectedBucketIds;

    public static ShotFragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),
                new TypeToken<Shot>(){});
        isLiking = true;

        swipeRefreshLayout.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(shot, this));

        new CheckLikeTask().execute();
        new LoadBucketsTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_BUCKET && resultCode == Activity.RESULT_OK) {
            List<String> chosenBucketIds = data.getStringArrayListExtra(
                    BucketListFragment.KEY_CHOSEN_BUCKET_IDS);
            List<String> addedBucketIds = new ArrayList<>();
            List<String> removedBucketIds = new ArrayList<>();
            for (String chosenBucketId : chosenBucketIds) {
                if (!collectedBucketIds.contains(chosenBucketId)) {
                    addedBucketIds.add(chosenBucketId);
                }
            }

            for (String collectedBucketId : collectedBucketIds) {
                if (!chosenBucketIds.contains(collectedBucketId)) {
                    removedBucketIds.add(collectedBucketId);
                }
            }

            new UpdateBucketTask(addedBucketIds, removedBucketIds).execute();
        }
    }

    private void setResult() {//to tell the ShotListFragment to update this shot information.
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>(){}));
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    public void bucket() {
        if (!Dribbble.isLoggedIn()) {
            Displayer.showOnSnackBar(getView(), formatNotLoggedInMessage("bucket"));
            return;
        }

        if (collectedBucketIds == null) {
            Displayer.showOnSnackBar(getView(), getString(R.string.shot_detail_loading_buckets));
        } else {
            Intent intent = new Intent(getContext(), BucketListActivity.class);
            intent.putExtra(BucketListFragment.KEY_EDITING_MODE, true);
            intent.putStringArrayListExtra(BucketListFragment.KEY_COLLECTED_BUCKET_IDS,
                    collectedBucketIds);
            startActivityForResult(intent, REQ_CODE_BUCKET);
        }
    }

    public void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shot.title + " " + shot.html_url);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_shot)));
    }

    public void like(@NonNull String shotId, boolean like) {
        if (!Dribbble.isLoggedIn()) {
            Displayer.showOnSnackBar(getView(), formatNotLoggedInMessage("like"));
            return;
        }
        if (!isLiking) {
            isLiking = true;
            new LikeTask(shotId, like).execute();
        }
    }

    private String formatNotLoggedInMessage(String action) {
        return getString(R.string.not_logged_in_error, " '" + action + "'");
    }

    /*******************************background tasks************************************************/
    private class LoadBucketsTask extends BackgroundTask<Void, Void, List<String>> {

        @Override
        protected List<String> doJob(Void... params) throws DribbbleException {
            List<Bucket> shotBuckets = Dribbble.getShotBuckets(shot.id);
            List<Bucket> userBuckets = Dribbble.getUserBuckets();

            Set<String> userBucketIds = new HashSet<>();
            for (Bucket userBucket : userBuckets) {
                userBucketIds.add(userBucket.id);
            }

            List<String> collectedBucketIds = new ArrayList<>();
            for (Bucket shotBucket : shotBuckets) {
                if (userBucketIds.contains(shotBucket.id)) {
                    collectedBucketIds.add(shotBucket.id);
                }
            }

            return collectedBucketIds;
        }

        @Override
        protected void onSuccess(List<String> result) {
            collectedBucketIds = new ArrayList<>(result);

            if (result.size() > 0) {
                shot.bucketed = true;
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class UpdateBucketTask extends BackgroundTask<Void, Void, Void> {

        private List<String> added;
        private List<String> removed;

        private UpdateBucketTask(@NonNull List<String> added,
                                 @NonNull List<String> removed) {
            this.added = added;
            this.removed = removed;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            for (String addedId : added) {
                Dribbble.addBucketShot(addedId, shot.id);
            }

            for (String removedId : removed) {
                Dribbble.removeBucketShot(removedId, shot.id);
            }
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {
            collectedBucketIds.addAll(added);
            collectedBucketIds.removeAll(removed);

            shot.bucketed = !collectedBucketIds.isEmpty();
            shot.buckets_count += added.size() - removed.size();

            recyclerView.getAdapter().notifyDataSetChanged();

            setResult();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class LikeTask extends BackgroundTask<Void, Void, Void> {

        private String id;
        private boolean like;

        public LikeTask(String id, boolean like) {
            this.id = id;
            this.like = like;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            if (like) {
                Dribbble.likeShot(id);
            } else {
                Dribbble.unlikeShot(id);
            }
            return null;
        }

        @Override
        protected void onSuccess(Void s) {
            isLiking = false;

            shot.liked = like;
            shot.likes_count += like ? 1 : -1;
            recyclerView.getAdapter().notifyDataSetChanged();

            setResult();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            isLiking = false;
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class CheckLikeTask extends BackgroundTask<Void, Void, Boolean> {

        @Override
        protected Boolean doJob(Void... params) throws DribbbleException {
            return Dribbble.isLikingShot(shot.id);
        }

        @Override
        protected void onSuccess(Boolean result) {
            isLiking = false;
            shot.liked = result;
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            isLiking = false;
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
