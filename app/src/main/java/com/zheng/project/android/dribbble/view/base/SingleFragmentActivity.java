package com.zheng.project.android.dribbble.view.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.zheng.project.android.dribbble.R;

public abstract class SingleFragmentActivity extends BaseActivity {

    @Override
    protected void onSavedInstanceStateIsNull() {
        setFragment(newFragment(), R.id.fragment_container);
    }

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_single_fragment);
    }

    @Override
    protected void onViewCreated() {
        if (isBackEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getActivityTitle());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isBackEnabled() && item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isBackEnabled() {return true;}

    @NonNull
    protected String getActivityTitle() {return "";}

    @NonNull
    protected abstract Fragment newFragment();
}
