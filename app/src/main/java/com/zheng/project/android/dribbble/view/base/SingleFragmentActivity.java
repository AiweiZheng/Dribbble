package com.zheng.project.android.dribbble.view.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zheng.project.android.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
