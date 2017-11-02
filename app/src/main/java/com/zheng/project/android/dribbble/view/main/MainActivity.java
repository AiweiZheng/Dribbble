package com.zheng.project.android.dribbble.view.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.BackgroundThread.BackgroundTask;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Auth;
import com.zheng.project.android.dribbble.dribbble.auth.AuthActivity;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;
import com.zheng.project.android.dribbble.utils.Displayer;
import com.zheng.project.android.dribbble.view.base.BaseActivity;
import com.zheng.project.android.dribbble.view.bucket_list.BucketListFragment;
import com.zheng.project.android.dribbble.view.settings.ThemeFragment;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

import java.io.IOException;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_nav) NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected Drawable getOverflowIcon() {
        return ContextCompat.getDrawable(this, R.drawable.ic_filter_white_24dp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSavedInstanceStateIsNull() {
        setFragment(ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR),
                R.id.main_fragment_container);
    }

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onViewCreated() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Dribbble.init(this);

        setupDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Auth.REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            new GetUserTask(authCode).execute();
        }
    }

    private void setupDrawer() {

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,          /* DrawerLayout object */
                R.string.open_drawer,         /* "open drawer" description */
                R.string.close_drawer         /* "close drawer" description */
        );

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.drawer_menu_item_popularity:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR);
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_menu_item_animated:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_ANIMATED);
                        setTitle(R.string.title_animated_gif);
                        break;
                    case R.id.drawer_menu_item_most_commented:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_MOST_COMMENTS);
                        setTitle(R.string.title_most_commented);
                        break;
                    case R.id.drawer_menu_item_most_recent:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_MOST_RECENT);
                        setTitle(R.string.title_most_recent);
                        break;
                    case R.id.drawer_menu_item_most_viewed:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_MOST_VIEWED);
                        setTitle(R.string.title_most_viewed);
                        break;
                    case R.id.drawer_menu_item_following:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_My_FOLLOWING);
                        setTitle(R.string.my_following_shots);
                        break;
                    case R.id.drawer_menu_item_likes:
                        setTitle(R.string.title_likes);
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_LIKED);
                        break;
                    case R.id.drawer_menu_item_buckets:
                        setTitle(R.string.title_buckets);
                        fragment = BucketListFragment.newInstance(null, false, null);
                        break;
                    case R.id.drawer_menu_item_themes:
                        setTitle(R.string.title_themes);
                        fragment = ThemeFragment.newInstance();
                        break;
                }
                drawerLayout.closeDrawers();

                if (fragment != null) {
                    setFragment(fragment, R.id.main_fragment_container);
                    return true;
                }

                return false;
            }
        });

        setupDrawerUI(Dribbble.isLoggedIn());
    }

    private void setupDrawerUI(boolean isLoggedIn) {
        setupNavHeader(isLoggedIn);
        SetupNavMenu(isLoggedIn);
    }

    private void SetupNavMenu(boolean isLoggedIn) {
        if (isLoggedIn) {
            setupNavMenuItemsForLoggedInUser();
        }
        else {
            setupNavMenuItemsForGuest();
        }
    }

    public void setupNavHeader(boolean isLoggedIn) {
        if (isLoggedIn) {
            setupNavHeaderForLoggedInUser();
        }
        else {
            setupNavHeaderForGuest();
        }
    }

    private void setupNavHeaderForLoggedInUser() {
        View headerView = navigationView.getHeaderView(0);

        (headerView.findViewById(R.id.nav_header_login_btn)).setVisibility(View.GONE);
        (headerView.findViewById(R.id.layout_for_loggedIn_user)).setVisibility(View.VISIBLE);

        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(
                Dribbble.getCurrentUser().name);

        ((SimpleDraweeView) headerView.findViewById(R.id.nav_header_user_picture))
                .setImageURI(Uri.parse(Dribbble.getCurrentUser().avatar_url));

        headerView.findViewById(R.id.nav_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dribbble.logout(MainActivity.this);
                setupNavHeaderForGuest();
                setupNavMenuItemsForGuest();
            }
        });
    }

    private void setupNavHeaderForGuest() {// not logged in user
        View headerView = navigationView.getHeaderView(0);
        (headerView.findViewById(R.id.nav_header_login_btn)).setVisibility(View.VISIBLE);
        (headerView.findViewById(R.id.layout_for_loggedIn_user)).setVisibility(View.GONE);
        (headerView.findViewById(R.id.nav_header_login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.openAuthActivity(MainActivity.this);
            }
        });
    }

    private void setupNavMenuItemsForLoggedInUser() {
        navigationView.getMenu().findItem(R.id.drawer_menu_item_likes).setVisible(true);
        navigationView.getMenu().findItem(R.id.drawer_menu_item_buckets).setVisible(true);
    }
    private void setupNavMenuItemsForGuest() {
        navigationView.getMenu().findItem(R.id.drawer_menu_item_likes).setVisible(false);
        navigationView.getMenu().findItem(R.id.drawer_menu_item_buckets).setVisible(false);
    }

    /*******************************background task********************/
    private class GetUserTask extends BackgroundTask<Void, Void, Void> {

        private String authCode;

        public GetUserTask(String authCode) {
            this.authCode = authCode;
        }
        @Override
        protected void onSuccess(Void aVoid) {

            setupDrawerUI(Dribbble.isLoggedIn());
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Displayer.showOnSnackBar(getWindow().getDecorView().getRootView(), e.getMessage());
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            //use code to fetch auth code for future API query.
            String token = null;
            try {
                token = Auth.fetchAccessToken(authCode);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            // store access token in SharedPreferences
            Dribbble.login(MainActivity.this, token);
            return null;
        }


        public void execute() {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
