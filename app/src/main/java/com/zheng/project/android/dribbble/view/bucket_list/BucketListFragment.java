package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.utils.Displayer;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.view.base.InfiniteFragment;
import com.zheng.project.android.dribbble.models.Bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class BucketListFragment extends InfiniteFragment<Bucket> {

    public static final int REQ_CODE_NEW_BUCKET = 100;
    public static final int REQ_CODE_DELETE_BUCKET = 200;
    public static final int REQ_CODE_EDIT_BUCKET = 300;

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

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(BucketListFragment.constructArgs(userId, isEditingMode, chosenBucketIds));
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
        adapter = new BucketListAdapter(getContext(),
                new ArrayList<Bucket>(),
                isEditingMode,
                new BucketOptionsMenu.EditBucketListener() {
                    @Override
                    public void onEditBucket(Bucket bucket) {
                        showEditBucketDialog(bucket.id, bucket.name, bucket.description);
                    }
                },
                new BucketOptionsMenu.DeleteBucketListener() {
                                                    @Override
                                                    public void onDeleteBucket(Bucket bucket) {
                                                       showDeleteBucketDialog(bucket);
                                                    }
                                               });

        return adapter;
    }

    @NonNull
    @Override
    protected View createView(@Nullable ViewGroup container) {
        return getLayoutInflater().inflate(R.layout.fragment_fab_recycler_view, container, false);
    }

    @Override
    protected void viewCreated() {
        userId = readUserIdFromArgs();
        isEditingMode = readEditingModeFromArgs();

        if (isEditingMode) {
            List<String> chosenBucketIdList = readCollectedBucketsIdFromArgs();
            if (chosenBucketIdList != null) {
                collectedBucketIdSet = new HashSet<>(chosenBucketIdList);
            }
        }
        else {
            collectedBucketIdSet = new HashSet<>();
        }

        setFloatingActionBarListener();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_NEW_BUCKET:
                    handleNewBucketTask(data);
                    break;
                case REQ_CODE_EDIT_BUCKET:
                    handleEditBucketTask(data);
                    break;
                case REQ_CODE_DELETE_BUCKET:
                    handleDeleteBucketTask(data);
                    break;
            }
        }

    }

    private void handleEditBucketTask(Intent data) {
        new UpdateBucketTask(data.getStringExtra(BucketDialogFragment.KEY_BUCKET_ID),
                data.getStringExtra(BucketDialogFragment.KEY_BUCKET_NAME),
                data.getStringExtra(BucketDialogFragment.KEY_BUCKET_DESCRIPTION)).execute();
    }

    private void handleNewBucketTask(Intent data) {
        String bucketName = data.getStringExtra(BucketDialogFragment.KEY_BUCKET_NAME);
        String bucketDescription = data.getStringExtra(BucketDialogFragment.KEY_BUCKET_DESCRIPTION);
        new NewBucketTask(bucketName, bucketDescription).execute();
    }

    private void handleDeleteBucketTask(Intent data) {
        String bucketId = data.getStringExtra(DeleteBucketConfirmationDialog.KEY_BUCKET_ID);
        String bucketName = data.getStringExtra(DeleteBucketConfirmationDialog.KEY_BUCKET_NAME);
        new DeleteBucketTask(bucketId, bucketName).execute();
    }

    @NonNull
    private static Bundle constructArgs(String userId,
                                        boolean isEditingMode,
                                        ArrayList<String> chosenBucketIds) {
        Bundle args = new Bundle();

        args.putString(KEY_USER_ID, userId);
        args.putBoolean(KEY_EDITING_MODE, isEditingMode);
        args.putStringArrayList(KEY_COLLECTED_BUCKET_IDS, chosenBucketIds);

        return args;
    }

    private String readUserIdFromArgs() {
        return getArguments().getString(KEY_USER_ID);
    }

    private boolean readEditingModeFromArgs() {
        return getArguments().getBoolean(KEY_EDITING_MODE);
    }

    private List<String> readCollectedBucketsIdFromArgs() {
        return getArguments().getStringArrayList(KEY_COLLECTED_BUCKET_IDS);
    }

    private void setFloatingActionBarListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewBucketDialog();
            }
        });
    }

    private void showNewBucketDialog() {
        BucketDialogFragment dialogFragment = BucketDialogFragment.newInstance(null);
        dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
        dialogFragment.show(getFragmentManager(), BucketDialogFragment.TAG);
    }

    private void showEditBucketDialog(@NonNull String bucketId,
                                      @NonNull String name,
                                      @NonNull String description) {
        Bundle args = new Bundle();
        args.putString(BucketDialogFragment.KEY_BUCKET_ID, bucketId);
        args.putString(BucketDialogFragment.KEY_BUCKET_NAME, name);
        args.putString(BucketDialogFragment.KEY_BUCKET_DESCRIPTION, description);

        BucketDialogFragment dialogFragment = BucketDialogFragment.newInstance(args);
        dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_EDIT_BUCKET);
        dialogFragment.show(getFragmentManager(), BucketDialogFragment.TAG);
    }

    private void showDeleteBucketDialog(@NonNull Bucket bucket) {
        Bundle args = new Bundle();
        args.putString(DeleteBucketConfirmationDialog.KEY_BUCKET_ID, bucket.id);
        args.putString(DeleteBucketConfirmationDialog.KEY_BUCKET_NAME, bucket.name);

        DeleteBucketConfirmationDialog dialogFragment
                = DeleteBucketConfirmationDialog.newInstance(args);

        dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_DELETE_BUCKET);
        dialogFragment.show(getFragmentManager(), DeleteBucketConfirmationDialog.TAG);
    }

    ///////////////////////////////////background task////////////////////////////////////////////
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
            scrollToTop();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Bucket doJob(Void... params) throws DribbbleException {
            return  Dribbble.newBucket(name, description);
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class UpdateBucketTask extends BackgroundTask<Void, Void, Void> {

        private String bucketId;
        private String name;
        private String description;

        public UpdateBucketTask(String bucketId, String name, String description) {
            this.bucketId = bucketId;
            this.name = name;
            this.description = description;
        }

        @Override
        protected void onSuccess(Void aVoid) {
            adapter.updateBucket(bucketId, name, description);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.updateBucket(bucketId, name, description);
            return null;
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    private class DeleteBucketTask extends BackgroundTask<Void, Void, Void> {

        private String bucketId;
        private String bucketName;

        public DeleteBucketTask (@NonNull String bucketId, @NonNull String bucketName) {
            this.bucketId = bucketId;
            this.bucketName = bucketName;
        }
        @Override
        protected void onSuccess(Void aVoid) {
            adapter.removeBucket(bucketId);
            Displayer.ShowOnToast(getContext(), "'" + bucketName + "' " + R.string.has_been_deleted);

        }

        @Override
        protected void onFailed(DribbbleException e) {
                Displayer.showOnSnackBar(getView(), e.getMessage());
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.deleteBucket(bucketId);
            return null;
        }

        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
