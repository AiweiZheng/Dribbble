package com.zheng.project.android.dribbble.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.HashMap;

public class Shot {
    public static final String IMAGE_HIDPI = "hidpi";
    private static final Object IMAGE_NORMAL = "normal";

    public String id;
    public String title;
    public String description;
    public String html_url;

    public int views_count;
    public int likes_count;
    public int buckets_count;
    public Date created_at;

    public HashMap<String, String> images;
    public boolean animated;
    public User user;
    public boolean bucketed;

    @NonNull
    public String getImageUrl() {
        if (images == null) {
            return "";
        }

        String url = images.containsKey(IMAGE_HIDPI) && images.get(IMAGE_HIDPI) != null
                ? images.get(IMAGE_HIDPI)
                : images.get(IMAGE_NORMAL);
        return url == null ? "" : url;
    }
}
