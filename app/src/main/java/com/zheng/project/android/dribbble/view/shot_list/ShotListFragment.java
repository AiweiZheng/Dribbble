package com.zheng.project.android.dribbble.view.shot_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.models.ShotQueryParameter;
import com.zheng.project.android.dribbble.utils.Displayer;
import com.zheng.project.android.dribbble.utils.ModelUtils;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.view.base.InfiniteFragment;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;

import java.util.ArrayList;
import java.util.List;

public class ShotListFragment extends InfiniteFragment<Shot> {

    public static final int REQ_CODE_SHOT = 100;

    public static final String KEY_LIST_TYPE = "list_type";
    public static final String KEY_BUCKET_ID = "bucket_id";

    public static final int LIST_TYPE_POPULAR = 1;
    public static final int LIST_TYPE_ANIMATED = 2;
    public static final int LIST_TYPE_MOST_COMMENTS = 3;
    public static final int LIST_TYPE_MOST_VIEWED = 4;
    public static final int LIST_TYPE_MOST_RECENT = 5;
    public static final int LIST_TYPE_LIKED = 6;
    public static final int LIST_TYPE_BUCKET = 7;

    private ShotQueryParameter shotQueryParameter = new ShotQueryParameter();
    private ShotListAdapter adapter;
    @NonNull
    public static ShotListFragment newInstance(int listType) {
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, listType);

        ShotListFragment fragment = new ShotListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newBucketListInstance(String bucketId) {
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, LIST_TYPE_BUCKET);
        args.putString(KEY_BUCKET_ID, bucketId);

        ShotListFragment fragment = new ShotListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Shot> fetchData(int listStyle, int page) throws DribbbleException {


        switch (listStyle) {
            case LIST_TYPE_POPULAR:
                shotQueryParameter.list = Dribbble.SHOTS_LIST_TYPE_DEFAULT;
                return Dribbble.getShots(page, shotQueryParameter);

            case LIST_TYPE_ANIMATED:
                shotQueryParameter.list = Dribbble.SHOTS_LIST_TYPE_ANIMATED;
                return Dribbble.getShots(page, shotQueryParameter);

            case LIST_TYPE_MOST_COMMENTS:
                shotQueryParameter.list = Dribbble.SHOTS_LIST_TYPE_DEFAULT;
                shotQueryParameter.sort = Dribbble.SHOTS_SORT_BY_COMMENTS;
                return Dribbble.getShots(page, shotQueryParameter);

            case LIST_TYPE_MOST_VIEWED:
                shotQueryParameter.list = Dribbble.SHOTS_LIST_TYPE_DEFAULT;
                shotQueryParameter.sort = Dribbble.SHOTS_SORT_BY_VIEWS;
                return Dribbble.getShots(page, shotQueryParameter);

            case LIST_TYPE_MOST_RECENT:
                shotQueryParameter.list = Dribbble.SHOTS_LIST_TYPE_DEFAULT;
                shotQueryParameter.sort = Dribbble.SHOTS_SORT_BY_RECENT;
                return Dribbble.getShots(page, shotQueryParameter);

            case LIST_TYPE_LIKED:
                setHasOptionsMenu(false);
                return Dribbble.getLikedShots(page);

            case LIST_TYPE_BUCKET:
                setHasOptionsMenu(false);
                String bucketId = getArguments().getString(KEY_BUCKET_ID);
                return Dribbble.getBucketShots(bucketId, page);
        }
        return null;
    }

    @Override
    protected List<Shot> refreshData() throws DribbbleException {
        return fetchData(getArguments().getInt(KEY_LIST_TYPE), 1);
    }

    @Override
    protected List<Shot> loadMoreData(int nextPage) throws DribbbleException {
        return fetchData(getArguments().getInt(KEY_LIST_TYPE), nextPage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_time_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.time_filter_menu_item_now:
                shotQueryParameter.timeFrame = Dribbble.SHOTS_AT_FROM_NOW;
                new GetShotTask(shotQueryParameter).execute();
                return true;
            case R.id.time_filter_menu_item_last_week:
                shotQueryParameter.timeFrame = Dribbble.SHOTS_AT_LAST_WEEK;
                new GetShotTask(shotQueryParameter).execute();
                return true;
            case R.id.time_filter_menu_item_last_month:
                shotQueryParameter.timeFrame = Dribbble.SHOTS_AT_LAST_MONTH;
                new GetShotTask(shotQueryParameter).execute();
                return true;
            case R.id.time_filter_menu_item_last_year:
                shotQueryParameter.timeFrame = Dribbble.SHOTS_AT_LAST_YEAR;
                new GetShotTask(shotQueryParameter).execute();
                return true;
            case R.id.time_filter_menu_item_all_time:
                shotQueryParameter.timeFrame = Dribbble.SHOTS_TILL_NOW;
                new GetShotTask(shotQueryParameter).execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected InfiniteAdapter createAdapter() {
        adapter = new ShotListAdapter(this, new ArrayList<Shot>());
        return adapter;
    }

    @NonNull
    @Override
    protected View createView(@Nullable ViewGroup container) {
        setHasOptionsMenu(true);
        return getLayoutInflater().inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SHOT && resultCode == Activity.RESULT_OK) {
            Shot updatedShot = ModelUtils.toObject(data.getStringExtra(ShotFragment.KEY_SHOT),
                    new TypeToken<Shot>(){});
            for (Shot shot : adapter.getData()) {
                if (TextUtils.equals(shot.id, updatedShot.id)) {
                    shot.likes_count = updatedShot.likes_count;
                    shot.buckets_count = updatedShot.buckets_count;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /*********************************backGround tasks***************************************/
    private class GetShotTask extends BackgroundTask<Void, Void, List<Shot>> {

        ShotQueryParameter parameter;

        public GetShotTask(ShotQueryParameter parameter) {
            this.parameter = parameter;
        }

        @Override
        protected void onSuccess(List<Shot> shots) {
            adapter.setData(shots);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected List<Shot> doJob(Void... params) throws DribbbleException {
            return   Dribbble.getShots(0, shotQueryParameter);
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
