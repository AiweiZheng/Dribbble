package com.zheng.project.android.dribbble;

import android.app.Application;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zheng.project.android.dribbble.models.Shot;
import com.zheng.project.android.dribbble.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DribbboApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
