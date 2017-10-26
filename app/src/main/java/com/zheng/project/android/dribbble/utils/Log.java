package com.zheng.project.android.dribbble.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Log {
    public static Snackbar error(View view, String msg) {
        return Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
    }
}
