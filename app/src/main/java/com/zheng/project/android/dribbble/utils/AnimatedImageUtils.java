package com.zheng.project.android.dribbble.utils;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class AnimatedImageUtils {

    // play gif automatically
    public static void autoPlayAnimations(String url, SimpleDraweeView view) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .build();
        view.setController(controller);
    }

}
