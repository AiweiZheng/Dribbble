package com.zheng.project.android.dribbble.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class AnimatedImageUtils {

    // play gif automatically
    public static void autoPlayAnimations(@NonNull  String url,
                                          @NonNull SimpleDraweeView view,
                                          @NonNull ProgressBar progressBar) {

        view.getHierarchy().setProgressBarImage(new AnimatedImageProgressBar(progressBar));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .build();
        view.setController(controller);
    }

    static class AnimatedImageProgressBar extends Drawable {
        private ProgressBar progressBar;
        private final static int FULL_DOWNLOADED = 10000;

        public AnimatedImageProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setMax(FULL_DOWNLOADED);
            this.progressBar.setProgress(0);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {}

        @Override
        public void setAlpha(@IntRange(from = 0, to = 255) int i) {}

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {}

        @Override
        public int getOpacity() {return PixelFormat.TRANSLUCENT;}

        @Override
        protected boolean onLevelChange(int level) {
            progressBar.setProgress(level);
            if (level == FULL_DOWNLOADED) {
                progressBar.setVisibility(View.GONE);
            }
            return true;
            // level is on a scale of 0-10,000
            // where 10,000 means fully downloaded
        }
    }
}

