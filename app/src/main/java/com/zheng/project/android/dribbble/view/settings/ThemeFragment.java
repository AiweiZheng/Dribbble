package com.zheng.project.android.dribbble.view.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.zheng.project.android.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeFragment extends Fragment{

    @BindView(R.id.theme_default) TextView defaultTheme;
    @BindView(R.id.theme_blue) TextView blueTheme;
    @BindView(R.id.theme_yellow) TextView yellowTheme;
    @BindView(R.id.theme_green) TextView greenTheme;
    @BindView(R.id.theme_purple) TextView purpleTheme;
    @BindView(R.id.theme_teal) TextView tealTheme;
    @BindView(R.id.theme_night_mode) TextView nightTheme;

    private Animation animation;
    private int selectedTheme;

    public static ThemeFragment newInstance() {
        return new ThemeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.theme, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView selectedThemeView = getThemeView(ThemeControl.getTheme());
        selectedThemeView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_white_24dp, 0);

        setupThemesChangeListeners();

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.text_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                ThemeControl.changeToTheme(getActivity(), selectedTheme);
            }
        });
    }

    private void setupThemesChangeListeners() {
        defaultTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeChanged(defaultTheme, ThemeControl.THEME_RED);
            }
        });

        blueTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(blueTheme, ThemeControl.THEME_BLUE);
            }
        });

        yellowTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(yellowTheme, ThemeControl.THEME_YELLOW);
            }
        });

        greenTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(greenTheme, ThemeControl.THEME_GREEN);
            }
        });

        purpleTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(purpleTheme, ThemeControl.THEME_PURPLE);
            }
        });

        tealTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(tealTheme, ThemeControl.THEME_TEAL);
            }
        });

        nightTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                themeChanged(nightTheme, ThemeControl.THEME_NIGHT);
            }
        });
    }
    private TextView getThemeView(int theme) {
        TextView selectedTheme;

       switch (theme) {
           case ThemeControl.THEME_RED:
               selectedTheme = defaultTheme;
               break;
           case ThemeControl.THEME_BLUE:
               selectedTheme = blueTheme;
               break;
           case ThemeControl.THEME_YELLOW:
               selectedTheme = yellowTheme;
               break;
           case ThemeControl.THEME_GREEN:
               selectedTheme = greenTheme;
               break;
           case ThemeControl.THEME_PURPLE:
               selectedTheme = purpleTheme;
               break;
           case ThemeControl.THEME_TEAL:
               selectedTheme = tealTheme;
               break;
           case ThemeControl.THEME_NIGHT:
               selectedTheme = nightTheme;
               break;
           default:
               selectedTheme = defaultTheme;
       }
       return selectedTheme;
    }

    private void themeChanged(TextView themeView, int theme) {
        selectedTheme = theme;

        getThemeView(ThemeControl.getTheme()).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        themeView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_done_white_24dp, 0);

        themeView.startAnimation(animation);
    }
}
