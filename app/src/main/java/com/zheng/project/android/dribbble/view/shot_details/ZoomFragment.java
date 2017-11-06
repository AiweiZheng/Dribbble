package com.zheng.project.android.dribbble.view.shot_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.utils.AnimatedImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoomFragment extends Fragment{

    public static final java.lang.String KEY_SHOT_IMAGE_URL = "shot_image_url";
    @BindView(R.id.shot_image) SimpleDraweeView shotImage;

    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new ZoomFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.shot_zoom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        AnimatedImageUtils.autoPlayAnimations(getArguments().getString(KEY_SHOT_IMAGE_URL), shotImage);
    }
}
