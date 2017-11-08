package com.zheng.project.android.dribbble.view.base;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomTransitionListener implements Transition.TransitionListener, Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) {}

    @Override
    public void onAnimationRepeat(Animator animation) {}

    @Override
    public void onTransitionStart(Transition transition) {}

    @Override
    public void onTransitionEnd(Transition transition) {}

    @Override
    public void onTransitionCancel(Transition transition) {}

    @Override
    public void onTransitionPause(Transition transition) {}

    @Override
    public void onTransitionResume(Transition transition) {}
}
