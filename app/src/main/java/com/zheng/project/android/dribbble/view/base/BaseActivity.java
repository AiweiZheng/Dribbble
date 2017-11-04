package com.zheng.project.android.dribbble.view.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.settings.ThemeControl;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeControl.onActivityCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        onCreateView();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        onViewCreated();

        if (savedInstanceState == null) {
            onSavedInstanceStateIsNull();
        }
        toolbar.setOverflowIcon(getOverflowIcon());
        toolbar.setSubtitle(getSubtitle());
    }

    protected void onViewCreated(){}
    protected void onSavedInstanceStateIsNull(){}
    protected String getSubtitle() {return "";}
    protected Drawable getOverflowIcon() {
        return ContextCompat.getDrawable(this, R.drawable.ic_move_vert_black_24dp);
    }
    protected abstract void onCreateView();

    public void setFragment(Fragment fragment, int container) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commit();
    }
}
