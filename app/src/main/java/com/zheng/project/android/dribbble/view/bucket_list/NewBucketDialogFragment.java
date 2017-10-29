package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
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

public class NewBucketDialogFragment extends BaseAlertDialog {

    public static final String KEY_BUCKET_NAME = "bucket_name";
    public static final String KEY_BUCKET_DESCRIPTION = "bucket_description";
    public static final String TAG = "newBucketDialogFragment";

    @BindView(R.id.new_bucket_name) EditText bucketName;
    @BindView(R.id.new_bucket_description) EditText bucketDescription;
    @BindView(R.id.bucket_name_required) TextView requiredText;

    public static NewBucketDialogFragment newInstance() {
        NewBucketDialogFragment newBucketDialogFragment = new NewBucketDialogFragment();
        newBucketDialogFragment.setCancelable(false);
        return newBucketDialogFragment;
    }

    @Override
    protected void onViewCreated() {
        setBucketNameListener();
    }

    @Override
    protected boolean isValidInput() {
        return !TextUtils.isEmpty(bucketName.getText().toString().trim());
    }

    @Override
    protected String getTitle() {
        return (String) getText(R.string.new_bucket_title);
    }

    @Override
    protected String getPositiveButtonText() {
        return (String) getText(R.string.new_bucket_create);
    }

    @Override
    protected String getNegativeButtonText() {
        return (String) getText(R.string.new_bucket_cancel);
    }

    @Override
    protected void onInputIsValid() {
        Intent result = new Intent();
        result.putExtra(KEY_BUCKET_NAME, bucketName.getText().toString());
        result.putExtra(KEY_BUCKET_DESCRIPTION, bucketDescription.getText().toString().trim());
        getTargetFragment().onActivityResult(BucketListFragment.REQ_CODE_NEW_BUCKET,
                Activity.RESULT_OK,
                result);
    }

    @Override
    protected View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.new_bucket_dialog_fragment, null);
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
