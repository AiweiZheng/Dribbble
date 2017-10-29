package com.zheng.project.android.dribbble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

        ShotViewHolder shotViewHolder = new ShotViewHolder(view);
        return shotViewHolder;
    }

    @Override
    protected void onBindView(@NonNull final BaseViewHolder vh, int position) {
        ShotViewHolder shotVh = (ShotViewHolder) vh;
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
    }
}
