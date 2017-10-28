package com.zheng.project.android.dribbble.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Displayer {
    public static void showOnSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

//    public static Snackbar info(View view, String info) {
//        return Snackbar.make(view, info, Snackbar.LENGTH_LONG);
//    }

    public static void ShowOnToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
