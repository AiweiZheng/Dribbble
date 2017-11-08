package com.zheng.project.android.dribbble.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.DrawableUtils;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class AnimatedImageUtils {

    // play gif automatically
    public static void autoPlayAnimations(@NonNull  String url,
                                          @NonNull SimpleDraweeView view) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .build();
        view.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        view.setController(controller);

//        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
//
//        }
//        GenericDraweeHierarchyBuilder builder =newGenericDraweeHierarchyBuilder(context.getResources());
//
//        AnimationDrawable drawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.loading_animation); 
//
//        GenericDraweeHierarchy hierarchy = builder.setPlaceholderImage(drawable).setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER).build();
//
//        simpleDraweeView.setHierarchy(hierarchy);
//
//        drawable.start();

    }
}

