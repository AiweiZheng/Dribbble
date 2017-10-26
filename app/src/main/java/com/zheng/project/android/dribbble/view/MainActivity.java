package com.zheng.project.android.dribbble.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.view.base.BaseActivity;
import com.zheng.project.android.dribbble.view.base.DataListFragment;
import com.zheng.project.android.dribbble.view.bucket_list.BucketListFragment;
import com.zheng.project.android.dribbble.view.shot_list.ShotListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_nav) NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
        setFragment(DataListFragmentFactory.getFragment(DataListFragmentFactory.SHOT_LIST_FRAGMENT),
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
        setupDrawer();
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
                    case R.id.drawer_menu_item_home:
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR);
                        setTitle(R.string.title_home);
                        break;

                    case R.id.drawer_menu_item_likes:
                        setTitle(R.string.title_likes);
                        fragment = ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_LIKED);
                        break;

                    case R.id.drawer_menu_item_buckets:
                        setTitle(R.string.title_buckets);
                        fragment = BucketListFragment.newInstance(null, false, null);
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

        setupNavHeader();
    }

    private void setupNavHeader() {
        View headerView = navigationView.getHeaderView(0);

        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(
                Dribbble.getCurrentUser().name);

        ((SimpleDraweeView) headerView.findViewById(R.id.nav_header_user_picture))
                .setImageURI(Uri.parse(Dribbble.getCurrentUser().avatar_url));

        headerView.findViewById(R.id.nav_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dribbble.logout(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
