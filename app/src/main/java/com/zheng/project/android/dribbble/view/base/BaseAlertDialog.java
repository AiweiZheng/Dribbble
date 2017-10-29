package com.zheng.project.android.dribbble.view.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseAlertDialog extends DialogFragment{

    public BaseAlertDialog() {
        setCancelable(getCancelable());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = onCreateView();
        ButterKnife.bind(this, view);

        onViewCreated();

        AlertDialog dialog = getAlertDialogBuilder(view).create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isValidInput()) {
                    return;
                }

                onInputIsValid();
                dismiss();
            }
        });

        return dialog;
    }

    private AlertDialog.Builder getAlertDialogBuilder(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle(getTitle())
                .setPositiveButton(getPositiveButtonText(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {}
                }) .setNegativeButton(getNegativeButtonText(), null);

        return builder;
    }


    protected abstract View onCreateView();
    protected abstract String getPositiveButtonText();

    protected boolean isValidInput() {return true;}
    protected void onInputIsValid(){}
    protected boolean getCancelable() {return false;}
    protected String getTitle() {return "";}
    protected String getNegativeButtonText() {return "Cancel";}

    protected void onViewCreated() {}
}
