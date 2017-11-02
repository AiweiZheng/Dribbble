package com.zheng.project.android.dribbble.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String timeAgo(Date createdTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        if (createdTime != null) {
            long agoTimeInMin = (new Date(System.currentTimeMillis()).getTime() - createdTime.getTime()) / 1000 / 60;
            //within 1 minute
            if (agoTimeInMin <= 1) {
                return "Just";
            } else if (agoTimeInMin <= 60) {
                //within 60 minutes
                return agoTimeInMin + " minutes ago";
            } else if (agoTimeInMin <= 60 * 24) {
                return agoTimeInMin / 60 + " hours ago";
            } else if (agoTimeInMin <= 60 * 24 * 2) {
                return agoTimeInMin / (60 * 24) + " days ago";
            } else {
                return format.format(createdTime);
            }
        } else {
            return format.format(new Date(0));
        }
    }
}
