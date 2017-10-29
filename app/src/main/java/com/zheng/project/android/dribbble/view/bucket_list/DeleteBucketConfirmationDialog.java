package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;

import com.zheng.project.android.dribbble.view.base.ConfirmationDialog;

public class DeleteBucketConfirmationDialog extends ConfirmationDialog {

    public static final String KEY_BUCKET_ID = "bucket_id";
    public static final String KEY_BUCKET_NAME = "bucket_name";

    public static final String TAG = "delete_bucket_confirmation_dialog";

    public static final DeleteBucketConfirmationDialog newInstance(String bucketId, String bucketName) {

        DeleteBucketConfirmationDialog dialog = new DeleteBucketConfirmationDialog(bucketId, bucketName);
        return dialog;
    }

    private String bucketId;
    private String bucketName;

    public DeleteBucketConfirmationDialog(String bucketId, String bucketName) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        message = "Are you sure to delete this bucket?";
    }

    @Override
    protected String getPositiveButtonText() {
        return "Delete";
    }

    @Override
    protected void onInputIsValid() {
        Intent result = new Intent();
        result.putExtra(KEY_BUCKET_ID, bucketId);
        result.putExtra(KEY_BUCKET_NAME, bucketName);
        getTargetFragment().onActivityResult(BucketListFragment.REQ_CODE_DELETE_BUCKET,
                Activity.RESULT_OK,
                result);
    }
}
