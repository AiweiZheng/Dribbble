package com.zheng.project.android.dribbble.view.shot_list;

import android.os.*;
import android.os.Bundle;;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.base.SpaceItemDecoration;
import com.zheng.project.android.dribbble.models.Shot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotListFragment extends Fragment {

    public static final int COUNT_PER_PAGE = 20;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private ShotListAdapter adapter;

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

        final android.os.Handler handler = new android.os.Handler();

        adapter = new ShotListAdapter(fakeData(0), new ShotListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      handler.post(new Runnable() {
                          @Override
                          public void run() {
                              List<Shot> moreData = fakeData(adapter.getDataCount() / COUNT_PER_PAGE);
                              adapter.append(moreData);
                              adapter.setShowLoading(moreData.size() >= COUNT_PER_PAGE);
                          }
                      });
                  }
              }).start();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<Shot> fakeData(int page) {
        List<Shot> shotList = new ArrayList<>();
        int count = page > 2 ? COUNT_PER_PAGE : 10;
        Random random = new Random();
        for (int i = 0; i < count; ++i) {
            Shot shot = new Shot();
            shot.viewCount = random.nextInt(10000);
            shot.likeCount = random.nextInt(200);
            shot.bucketCount = random.nextInt(50);
            shotList.add(shot);
        }
        return shotList;
    }
}
