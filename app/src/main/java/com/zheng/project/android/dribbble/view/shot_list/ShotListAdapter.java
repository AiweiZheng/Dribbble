package com.zheng.project.android.dribbble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.InfiniteAdapter;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.view.shot_details.ShotActivity;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.util.List;

public class ShotListAdapter extends InfiniteAdapter<Shot> {

    public static final String TRANSITION_SHARED_ITEM_NAME_SHOT_IMAGE = "shared_item_shot_image";
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

        Picasso.with(shotVh.itemView.getContext())
                .load(shot.getImageUrl())
                .noFade()
                .into(shotVh.shotImage);

        if (shot.animated) {
            shotVh.gifText.setVisibility(View.VISIBLE);
        }
        else {
            shotVh.gifText.setVisibility(View.GONE);
        }

        ViewCompat.setTransitionName(shotVh.shotImage, shot.title);

        shotVh.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = vh.itemView.getContext();
                Intent intent = new Intent(context, ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT,
                        ModelUtils.toString(shot, new TypeToken<Shot>() {
                        }));
                intent.putExtra(TRANSITION_SHARED_ITEM_NAME_SHOT_IMAGE, ViewCompat.getTransitionName(shotVh.shotImage));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        shotListFragment.getActivity(),
                        shotVh.shotImage,
                        ViewCompat.getTransitionName(shotVh.shotImage));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    shotListFragment.startActivity(intent, options.toBundle());
                }
                else {
                    shotListFragment.startActivity(intent);
                }

            }
        });

    }
}
