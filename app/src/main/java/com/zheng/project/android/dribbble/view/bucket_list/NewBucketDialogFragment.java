package com.zheng.project.android.dribbble.view.bucket_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewBucketDialogFragment extends DialogFragment {

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.new_bucket_dialog_fragment, null);
        ButterKnife.bind(this, view);

        setBucketNameListener();

        AlertDialog dialog = getAlertDialogBuilder(view).create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isValidInput()) {
                    return;
                }

                Intent result = new Intent();
                result.putExtra(KEY_BUCKET_NAME, bucketName.getText().toString());
                result.putExtra(KEY_BUCKET_DESCRIPTION, bucketDescription.getText().toString().trim());
                getTargetFragment().onActivityResult(BucketListFragment.REQ_CODE_NEW_BUCKET,
                        Activity.RESULT_OK,
                        result);

                dismiss();
            }
        });

        return dialog;
    }


    private boolean isValidInput() {
        return !TextUtils.isEmpty(bucketName.getText().toString().trim());
    }

    public AlertDialog.Builder getAlertDialogBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle(R.string.new_bucket_title)
                .setPositiveButton(R.string.net_bucket_create, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {}
                }).setNegativeButton(R.string.new_bucket_cancel, null);

        return builder;
    }

    private void setBucketNameListener() {

        bucketName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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
