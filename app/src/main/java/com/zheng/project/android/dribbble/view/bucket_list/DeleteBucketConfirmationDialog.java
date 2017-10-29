package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zheng.project.android.dribbble.view.base.ConfirmationDialog;

public class DeleteBucketConfirmationDialog extends ConfirmationDialog {

    public static final String KEY_BUCKET_ID = "bucket_id";
    public static final String KEY_BUCKET_NAME = "bucket_name";

    public static final String TAG = "delete_bucket_confirmation_dialog";

    public static final DeleteBucketConfirmationDialog newInstance(@NonNull Bundle args) {
        DeleteBucketConfirmationDialog dialog = new DeleteBucketConfirmationDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public DeleteBucketConfirmationDialog() {
        message = "Are you sure to delete this bucket?";
    }

    @Override
    protected String getPositiveButtonText() {
        return "Delete";
    }

    @Override
    protected void onInputIsValid() {
        Intent result = new Intent();
        result.putExtra(KEY_BUCKET_ID,  getArguments().getString(KEY_BUCKET_ID));
        result.putExtra(KEY_BUCKET_NAME, getArguments().getString(KEY_BUCKET_NAME));
        getTargetFragment().onActivityResult(BucketListFragment.REQ_CODE_DELETE_BUCKET,
                Activity.RESULT_OK,
                result);
    }
}
