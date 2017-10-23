package com.zheng.project.android.dribbble.view.shot_list;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.base.SpaceItemDecoration;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    public static final int COUNT_PER_PAGE = 20;

    private ShotListAdapter adapter;

    public static ShotListFragment newInstance() {return new ShotListFragment();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                new LoadShotTask(true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));

        adapter = new ShotListAdapter(fakeData(0), new ShotListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadShotTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        recyclerView.setAdapter(adapter);
    }
    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {

        private boolean refresh = false;

        public LoadShotTask(boolean refresh) {
            this.refresh = refresh;
        }
        public LoadShotTask(){ this.refresh = false;}

        @Override
        protected List<Shot> doInBackground(Void... voids) {

            List<Shot> moreData;
            if (refresh) {
                moreData = fakeData(0);
            }
            else {
                moreData = fakeData(adapter.getDataCount() / COUNT_PER_PAGE);
            }
            return moreData;
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if (shots == null) {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (refresh) {
                adapter.setData(shots);
                swipeRefreshLayout.setRefreshing(false);
            }
            else {
                adapter.append(shots);
                adapter.setShowLoading(shots.size() >= COUNT_PER_PAGE);
                swipeRefreshLayout.setEnabled(true);
            }
        }
    }

    private List<Shot> fakeData(int page) {
        List<Shot> shotList = new ArrayList<>();
        Random random = new Random();

        int count = page < 2 ? COUNT_PER_PAGE : 10;

        for (int i = 0; i < count; ++i) {
            Shot shot = new Shot();
            shot.title = "shot" + i;
            shot.viewsCount = random.nextInt(10000);
            shot.likesCount = random.nextInt(200);
            shot.bucketsCount = random.nextInt(50);
            shot.description = makeDescription();

            shot.images = new HashMap<>();
            shot.images.put(Shot.IMAGE_HIDPI, imageUrls[random.nextInt(imageUrls.length)]);

            shot.user = new User();
            shot.user.name = shot.title + " author";

            shotList.add(shot);
        }
        return shotList;
    }

    private static final String[] words = {
            "bottle", "bowl", "brick", "building", "bunny", "cake", "car", "cat", "cup",
            "desk", "dog", "duck", "elephant", "engineer", "fork", "glass", "griffon", "hat", "key",
            "knife", "lawyer", "llama", "manual", "meat", "monitor", "mouse", "tangerine", "paper",
            "pear", "pen", "pencil", "phone", "physicist", "planet", "potato", "road", "salad",
            "shoe", "slipper", "soup", "spoon", "star", "steak", "table", "terminal", "treehouse",
            "truck", "watermelon", "window"
    };

    private static final String[] imageUrls = {
            "https://d13yacurqjgara.cloudfront.net/users/58851/screenshots/3400841/dribbble_pretoria-04.png",
            "https://d13yacurqjgara.cloudfront.net/users/41719/screenshots/3400864/octowheel.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/1008875/screenshots/3399601/old-pc.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/4381/screenshots/3400780/dribbble-1.png",
            "https://d13yacurqjgara.cloudfront.net/users/559871/screenshots/3401056/gradient_fox.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/79723/screenshots/3401386/untitled-9-01.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/698/screenshots/3401039/ss-2017-cover.png",
            "https://d13yacurqjgara.cloudfront.net/users/45389/screenshots/3400936/portfolium-spaceman.png",
            "https://d13yacurqjgara.cloudfront.net/users/65767/screenshots/3400922/peter_deltondo_virta_health_iphone_responsive_mobile_menu_2x.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/203446/screenshots/3400931/bitmap.png",
            "https://d13yacurqjgara.cloudfront.net/users/235360/screenshots/3400791/how-to.png",
            "https://d13yacurqjgara.cloudfront.net/users/58267/screenshots/3401160/people-socks-rebranding.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/363877/screenshots/3400983/gentle-bird-w.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/33298/screenshots/3400699/dribhat2.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/879147/screenshots/3401051/aaa.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/98561/screenshots/3401583/new_user_experience_style_frames_1x.png",
            "https://d13yacurqjgara.cloudfront.net/users/371094/screenshots/3401298/richkid.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/875337/screenshots/3400965/bg-plane.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/1365782/screenshots/3399506/new_copy_18.png",
            "https://d13yacurqjgara.cloudfront.net/users/44338/screenshots/3401460/aw1_drib.png"
    };

    private static String makeDescription() {
        return TextUtils.join(" ", words);
    }
}
