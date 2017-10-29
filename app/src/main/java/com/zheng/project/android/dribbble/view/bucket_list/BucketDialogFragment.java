package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseAlertDialog;

import butterknife.BindView;

public class BucketDialogFragment extends BaseAlertDialog {

    public static final  String KEY_BUCKET_ID = "bucket_id";
    public static final String KEY_BUCKET_NAME = "bucket_name";
    public static final String KEY_BUCKET_DESCRIPTION = "bucket_description";
    public static final String TAG = "newBucketDialogFragment";

    @BindView(R.id.new_bucket_name) EditText bucketName;
    @BindView(R.id.new_bucket_description) EditText bucketDescription;
    @BindView(R.id.bucket_name_required) TextView requiredText;

    private boolean isEditMode;

    public static BucketDialogFragment newInstance(@NonNull Bundle args) {
        BucketDialogFragment bucketDialogFragment = new BucketDialogFragment();
        bucketDialogFragment.setArguments(args);
        bucketDialogFragment.setCancelable(false);
        return bucketDialogFragment;
    }

    @Override
    protected void onViewCreated() {
        setBucketNameListener();
        if (getArguments() != null) {
            isEditMode = true;
            setupUI();
        }
    }

    @Override
    protected boolean isValidInput() {
        return !TextUtils.isEmpty(bucketName.getText().toString().trim());
    }

    @Override
    protected String getTitle() {
        if (isEditMode) {
            return getString(R.string.bucket_edit, "'" + bucketName.getText().toString() + "'");
        }
        return getString(R.string.new_bucket_title);
    }

    @Override
    protected String getPositiveButtonText() {
        if (isEditMode) {
            return getString(R.string.save);
        }
        return getString(R.string.new_bucket_create);
    }

    @Override
    protected String getNegativeButtonText() {
        return getString(R.string.new_bucket_cancel);
    }

    @Override
    protected void onInputIsValid() {
        Intent result = new Intent();
        int requestCode = BucketListFragment.REQ_CODE_NEW_BUCKET;

        if (isEditMode) {
            result.putExtra(KEY_BUCKET_ID, getArguments().getString(KEY_BUCKET_ID));
            requestCode = BucketListFragment.REQ_CODE_EDIT_BUCKET;
        }

        result.putExtra(KEY_BUCKET_NAME, bucketName.getText().toString());
        result.putExtra(KEY_BUCKET_DESCRIPTION, bucketDescription.getText().toString().trim());
        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK, result);
    }

    @Override
    protected View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.new_bucket_dialog_fragment, null);
    }

    private void setupUI() {
        bucketName.setText(getArguments().getString(KEY_BUCKET_NAME));
        bucketName.setSelection(bucketName.getText().length());//put the cursor on the end.
        bucketDescription.setText(getArguments().getString(KEY_BUCKET_DESCRIPTION));
    }

    private void setBucketNameListener() {

        bucketName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidInput()) {
                    requiredText.setVisibility(View.GONE);
                }
                else {
                    requiredText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
