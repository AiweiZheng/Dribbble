package com.zheng.project.android.dribbble.shot_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.base.SpaceItemDecoration;
import com.zheng.project.android.dribbble.models.Shot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static ShotListFragment newInstance() {return new ShotListFragment();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));
        recyclerView.setAdapter(new ShotListAdapter(fakeData()));
    }

    private List<Shot> fakeData() {
        List<Shot> shotList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; ++i) {
            Shot shot = new Shot();
            shot.viewCount = random.nextInt(10000);
            shot.likeCount = random.nextInt(200);
            shot.bucketCount = random.nextInt(50);
            shotList.add(shot);
        }
        return shotList;
    }
}
