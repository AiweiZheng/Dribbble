package com.zheng.project.android.dribbble.view.settings;

import android.app.Activity;
import android.content.Intent;

import com.zheng.project.android.dribbble.R;

public class ThemeControl {

    private static int theme = 0;

    public final static int THEME_RED = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_YELLOW = 2;
    public final static int THEME_GREEN = 3;
    public final static int THEME_PURPLE = 4;
    public final static int THEME_TEAL = 5;

    public static void changeToTheme(Activity activity, int theme) {
        ThemeControl.theme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static int getTheme() {
        return theme;
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (theme) {
            default:
            case THEME_RED:
                activity.setTheme(R.style.AppTheme_red);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.AppTheme_blue);
                break;
            case THEME_YELLOW:
                activity.setTheme(R.style.AppTheme_yellow);
                break;
            case THEME_GREEN:
                activity.setTheme(R.style.AppTheme_green);
                break;
            case THEME_PURPLE:
                activity.setTheme(R.style.AppTheme_purple);
                break;
            case THEME_TEAL:
                activity.setTheme(R.style.AppTheme_teal);
                break;
        }
    }

}
