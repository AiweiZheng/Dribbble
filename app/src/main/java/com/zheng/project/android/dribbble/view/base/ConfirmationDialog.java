package com.zheng.project.android.dribbble.view.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;

import butterknife.BindView;

public abstract class ConfirmationDialog extends BaseAlertDialog {

    @BindView(R.id.confimation_text) TextView confirmationText;

    protected String message;

    @Override
    protected View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.confirmation_dialog, null);
    }

    @Override
    protected void onViewCreated() {
        confirmationText.setText(message);
    }
}
