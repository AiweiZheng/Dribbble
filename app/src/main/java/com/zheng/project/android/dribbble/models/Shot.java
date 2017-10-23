package com.zheng.project.android.dribbble.models;

import android.support.annotation.Nullable;

import java.util.HashMap;

public class Shot {
    public static final String IMAGE_HIDPI = "hidpi";
    private static final Object IMAGE_NORMAL = "normal";

    public String title;
    public int viewsCount;
    public int likesCount;
    public int bucketsCount;
    public String description;
    public HashMap<String, String> images;
    public boolean animated;
    public User user;

    @Nullable
    public String getImageUrl() {
        if (images == null) {
            return null;
        } else if (animated) {
            return images.get(IMAGE_NORMAL);
        }

        return images.containsKey(IMAGE_HIDPI)
                ? images.get(IMAGE_HIDPI)
                : images.get(IMAGE_NORMAL);
    }
}
