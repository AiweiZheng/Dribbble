package com.zheng.project.android.dribbble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseViewHolder;
import com.zheng.project.android.dribbble.view.base.DataListAdapter;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.view.shot_details.ShotActivity;
import com.zheng.project.android.dribbble.view.shot_details.ShotFragment;
import com.zheng.project.android.dribbble.utils.ModelUtils;

import java.util.List;

public class ShotListAdapter extends DataListAdapter<Shot>{

    public ShotListAdapter(List<Shot> shots) {
        super(shots);
    }

    @Override
    protected BaseViewHolder onCreateView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shot, parent, false);

        ShotViewHolder shotViewHolder = new ShotViewHolder(view);
        return shotViewHolder;
    }

    @Override
    protected void onBindView(@NonNull final BaseViewHolder vh, @NonNull final Shot data) {
        ShotViewHolder shotVh = (ShotViewHolder) vh;

        shotVh.bucketCount.setText(String.valueOf((data.buckets_count)));
        shotVh.viewCount.setText(String.valueOf((data.views_count)));
        shotVh.likeCount.setText(String.valueOf((data.likes_count)));

        // play gif automatically
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(data.getImageUrl()))
                .setAutoPlayAnimations(true)
                .build();
        shotVh.shotImage.setController(controller);

        shotVh.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = vh.itemView.getContext();
                Intent intent = new Intent(context, ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT,
                        ModelUtils.toString(data, new TypeToken<Shot>() {
                        }));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE, data.title);

                context.startActivity(intent);
            }
        });
    }
}
