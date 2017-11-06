package com.zheng.project.android.dribbble.view.shot_list;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.view.shot_details.ShotActivity;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.util.List;

public class ShotListAdapter extends InfiniteAdapter<Shot> {

    private ShotListFragment shotListFragment;

    public ShotListAdapter(@NonNull ShotListFragment shotListFragment, List<Shot> shots) {
        super(shotListFragment.getContext(), shots);
        this.shotListFragment = shotListFragment;
    }

    @Override
    protected BaseViewHolder onCreateView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shot, parent, false);

        return new ShotViewHolder(view);
    }

    @Override
    protected void onBindView(@NonNull final BaseViewHolder vh, int position) {
        final ShotViewHolder shotVh = (ShotViewHolder) vh;
        final Shot shot = getData().get(position);

        shotVh.bucketCount.setText(String.valueOf((shot.buckets_count)));
        shotVh.viewCount.setText(String.valueOf((shot.views_count)));
        shotVh.likeCount.setText(String.valueOf((shot.likes_count)));
        shotVh.shotImage.setImageURI(Uri.parse(shot.getImageUrl()));
        if (shot.animated) {
            shotVh.gifText.setVisibility(View.VISIBLE);
        }
        else {
            shotVh.gifText.setVisibility(View.GONE);
        }
        shotVh.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = vh.itemView.getContext();
                Intent intent = new Intent(context, ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT,
                        ModelUtils.toString(shot, new TypeToken<Shot>() {
                        }));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                shotListFragment.startActivityForResult(intent, ShotListFragment.REQ_CODE_SHOT);
            }
        });

       // shotListFragment.resetAnimation();
     //   shotListFragment.scheduleLayoutAnimation();

//        ConstraintLayout constraintLayout = ((ShotViewHolder) vh).itemView.findViewById(R.id.shot_constraintLayout);
//
//        ConstraintSet constraintSet2 = new ConstraintSet();
//        constraintSet2.clone(constraintLayout);
//        constraintSet2.centerVertically(R.id.shot_image, 0);
//
//        TransitionManager.beginDelayedTransition(constraintLayout,  new Slide());
//        shotVh.shotImage.setVisibility(View.VISIBLE);
//        constraintSet2.applyTo(constraintLayout);
    }
}
