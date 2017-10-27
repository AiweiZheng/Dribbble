package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.utils.Log;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.view.base.InfiniteFragment;
import com.zheng.project.android.dribbble.models.Bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class BucketListFragment extends InfiniteFragment<Bucket> {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_EDITING_MODE = "editing_mode";
    public static final String KEY_COLLECTED_BUCKET_IDS = "collected_bucket_ids";
    public static final String KEY_CHOSEN_BUCKET_IDS = "chosen_bucket_ids";

    private String userId;
    private boolean isEditingMode;
    private HashSet<String> collectedBucketIdSet;

    private BucketListAdapter adapter;
    @BindView(R.id.fab) FloatingActionButton fab;

    public static BucketListFragment newInstance(@Nullable String userId,
                                                 boolean isEditingMode,
                                                 @Nullable ArrayList<String> chosenBucketIds) {
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userId);
        args.putBoolean(KEY_EDITING_MODE, isEditingMode);
        args.putStringArrayList(KEY_COLLECTED_BUCKET_IDS, chosenBucketIds);

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected void onDataFetched(List<Bucket> buckets) {
        for (Bucket bucket : buckets) {
            if (collectedBucketIdSet.contains(bucket.id)) {
                bucket.isChosen = true;
            }
        }
    }


    @NonNull
    @Override
    protected List<Bucket> refreshData() throws DribbbleException {
        if (userId == null) {
            return Dribbble.getUserBuckets(1);
        }
        return Dribbble.getUserBuckets(userId, 1);
    }

    @NonNull
    @Override
    protected List<Bucket> loadMoreData(int dataSize) throws DribbbleException {
        if (userId == null) {
            return Dribbble.getUserBuckets(dataSize / Dribbble.COUNT_PER_PAGE + 1);
        }
        return Dribbble.getUserBuckets(userId, dataSize / Dribbble.COUNT_PER_PAGE + 1);
    }

    @NonNull
    @Override
    protected InfiniteAdapter createAdapter() {
        adapter = new BucketListAdapter(getContext(), new ArrayList<Bucket>(), isEditingMode);
        return adapter;
    }

    @NonNull
    @Override
    protected View createView(@Nullable ViewGroup container) {
        return getLayoutInflater().inflate(R.layout.fragment_fab_recycler_view, container, false);
    }

    @Override
    protected void viewCreated() {

        final Bundle args = getArguments();
        userId = args.getString(KEY_USER_ID);
        isEditingMode = args.getBoolean(KEY_EDITING_MODE);

        if (isEditingMode) {
            List<String> chosenBucketIdList = args.getStringArrayList(KEY_COLLECTED_BUCKET_IDS);
            if (chosenBucketIdList != null) {
                collectedBucketIdSet = new HashSet<>(chosenBucketIdList);
            }
        }
        else {
            collectedBucketIdSet = new HashSet<>();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewBucketDialogFragment dialogFragment = NewBucketDialogFragment.newInstance();
                dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
                dialogFragment.show(getFragmentManager(), NewBucketDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_NAME);
            String bucketDescription = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(bucketName)) {
                new NewBucketTask(bucketName, bucketDescription).execute();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isEditingMode) {
            inflater.inflate(R.menu.bucket_list_choose_mode_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            ArrayList<String> chosenBucketIds = new ArrayList<>();
            for (Bucket bucket : adapter.getData()) {
                if (bucket.isChosen) {
                    chosenBucketIds.add(bucket.id);
                }
            }

            Intent result = new Intent();
            result.putStringArrayListExtra(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class NewBucketTask extends BackgroundTask<Void, Void, Bucket> {

        private String name;
        private String description;

        public NewBucketTask(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        protected void onSuccess(Bucket bucket) {
            bucket.isChosen = true;
            adapter.prepend(Collections.singletonList(bucket));
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Log.error(getView(), e.getMessage());
        }

        @Override
        protected Bucket doJob(Void... params) throws DribbbleException {
            return  Dribbble.newBucket(name, description);
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
